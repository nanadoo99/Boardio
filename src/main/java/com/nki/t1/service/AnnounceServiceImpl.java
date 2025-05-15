package com.nki.t1.service;

import com.nki.t1.component.*;
import com.nki.t1.dao.AnnounceDao;
import com.nki.t1.dao.FileAnnounceDao;
import com.nki.t1.dao.FileCkEditorDao;
import com.nki.t1.dao.FileCkEditorDaoAnnounceImpl;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.exception.InvalidAnnounceException;
import com.nki.t1.exception.InvalidFileException;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 예외처리할 것.
@Slf4j
@Service
public class AnnounceServiceImpl implements AnnounceService {

    private final CkEditorFileService ckEditorFileService;
    private final AbstractS3BaseMultiFileUploader baseMultiFileUploader;
    private final FileCkEditorDao fileCkEditorDao;
    private final AnnounceDao announceDao;
    private final FileAnnounceDao fileAnnounceDao;
    private final SessionUtils sessionUtils;

    public AnnounceServiceImpl(@Qualifier("s3BaseMultiFileUploaderAnnounce")
                               S3BaseMultiFileUploaderAnnounce baseMultiFileUploader,
                                FileCkEditorDaoAnnounceImpl fileCkEditorDao,
                                AnnounceDao announceDao,
                                FileAnnounceDao fileAnnounceDao,
                                @Qualifier("ckEditorFileServiceAnnounceImpl")
                                CkEditorFileService ckEditorFileService,
                                SessionUtils sessionUtils) {
        this.baseMultiFileUploader = baseMultiFileUploader;
        this.ckEditorFileService = ckEditorFileService;
        this.fileCkEditorDao = fileCkEditorDao;
        this.announceDao = announceDao;
        this.fileAnnounceDao = fileAnnounceDao;
        this.sessionUtils = sessionUtils;
    }

    // ckEditor 이미지 업로드
    @Override
    public FileDto ckFileUpload(MultipartFile file) throws IOException {
        return ckEditorFileService.upload(file);
    }


    @Override
    public int countAnnounceAdmin(SearchCondition sc) {
        return announceDao.countAnnounceAdmin(sc);
    }

    @Override
    public List<AnnounceDto> getAnnouncePageAdmin(SearchCondition sc) {
        sc.setUno(sessionUtils.getUserSecurityDto().getUno());
        return announceDao.getAnnouncePageAdmin(sc);
    }

    @Override
    public AnnounceDto selectAnnounceAdmin(int ano) {
        return announceDao.selectAnnounceAdmin(ano);
    }

    // 공지 생성
    @Override
    @Transactional
    public void createAnnounce(AnnounceDto announceDto) throws IOException {
        List<FileDto> uploadedFileList = null;

        try {

            // 검색을 위한 content 텍스트만을 추출
            announceDto.extractContentTxtFromContent();

            // db: 제목 및 내용 저장
            if (announceDao.insertAnnounce(announceDto) != 1) {
                throw new InvalidAnnounceException(ErrorType.POST_NOT_CREATED);
            }
            // 파일 서버: 업로드
            uploadedFileList = baseMultiFileUploader.fileListUpload(announceDto.getAttachedFiles());
            // db: 파일 내용 저장
            for (FileDto fileDto : uploadedFileList) {
                fileDto.setPostIdx(announceDto.getAno());
                fileAnnounceDao.insertFile(fileDto);
            }

            // 이미지 db 저장
            ckEditorFileService.saveImage(announceDto);
        } catch (InvalidAnnounceException e) {
            log.error("InvalidPostException : ", e);
            e.addObject(announceDto);
            throw e;
        } catch (Exception e) {
            // 예외 발생 시 파일 삭제 처리
            baseMultiFileUploader.fileListDelete(uploadedFileList, false);
            throw e;
        }
    }

    @Override
    public boolean isAnnouncePosted(int ano) {
        AnnounceDto announceDto = announceDao.selectAnnounceAdmin(ano);
        if (announceDto == null) {
            return false; // 공지사항이 존재하지 않음
        }

        LocalDate postedAt = announceDto.getPostedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        return postedAt.isBefore(today) || postedAt.isEqual(today);
    }

    @Transactional
    @Override
    public void updateAnnounce(AnnounceDto announceDto) {
        try {
            // 수정 중 삭제된 이미지 - db 및 서버에서 삭제 & 새로 추가된 이미지 - db 저장
            ckEditorFileService.fileSyncForPostUpdate(announceDto);

            // === 게시글 업로드
            log.info("게시글 업데이트 시작 - 게시글 ID: {}", announceDto.getAno());
            announceDto.extractContentTxtFromContent();
            announceDao.updateAnnounce(announceDto);
            log.info("게시글 업데이트 완료 - 게시글 ID: {}", announceDto.getAno());

            // === 파일 업로드

            // 새로 등록된 파일
            syncNewlyAttachedFileForAnnounceUpdate(announceDto);

            // 삭제된 파일
            syncDeletedFileForAnnounceUpdate(announceDto);

        } catch (Exception e) {
            log.error("게시글 업데이트 실패 - 게시글 ID: {}, 오류 메시지: {}", announceDto.getAno(), e.getMessage());
            throw new InvalidAnnounceException(ErrorType.ANNOUNCE_NOT_UPDATED, announceDto);
        }
    }

    private void syncDeletedFileForAnnounceUpdate(AnnounceDto announceDto) throws IOException {
        if (announceDto.getFnoList() != null && announceDto.getFnoList().size() > 0) {
            log.info("파일 삭제 처리 시작 - 삭제될 파일 FNO 리스트: {}", announceDto.getFnoList());

            // fno로 파일 리스트 받아오기
            List<FileDto> fileDtoList = fileAnnounceDao.selectFileListByFnoListAdmin(announceDto.getFnoList());
            log.info("파일 리스트 조회 완료 - 조회된 파일: {}", fileDtoList.size());

            // 파일 리스트로 삭제
            baseMultiFileUploader.fileListDelete(fileDtoList, true);
            log.info("파일 삭제 완료 - 삭제된 파일: {}", fileDtoList.size());

            // 삭제된 파일-  db 삭제
            fileAnnounceDao.deleteFileListByFnoListAdmin(announceDto.getFnoList());
            log.info("파일 정보 DB 삭제 완료 - 삭제된 FNO 리스트: {}", announceDto.getFnoList());
        } else {
            log.info("삭제할 파일이 없습니다.");
        }
    }

    private void syncNewlyAttachedFileForAnnounceUpdate(AnnounceDto announceDto) throws IOException {
        if (announceDto.getAttachedFiles() != null) {
            List<MultipartFile> attachedFiles = announceDto.getAttachedFiles().stream().filter(file -> !file.isEmpty()).collect(Collectors.toList());
            if (attachedFiles.size() > 0) {
                log.info("새로 등록된 파일 {}개 처리 시작 - 게시글 ID: {}", attachedFiles.size(), announceDto.getAno());
                // 새로 등록된 파일-  서버 업로드
                List<FileDto> uploadedFileList = baseMultiFileUploader.fileListUpload(attachedFiles);
                log.info("파일 업로드 완료 - 업로드된 파일: {}", uploadedFileList.size());

                // 새로 등록된 파일-  db 업로드
                for (FileDto fileDto : uploadedFileList) {
                    fileDto.setPostIdx(announceDto.getAno());
                    fileAnnounceDao.insertFile(fileDto);
                    log.info("파일 정보 저장 완료 - 파일명: {}, 게시글 ID: {}", fileDto.getFileOrgNm(), announceDto.getAno());
                }
            }
        } else {
            log.info("새로 등록된 파일이 없습니다.");
        }
    }


    @Override
    @Transactional
    public void deleteAnnounce(AnnounceDto announceDto) {
        try {
            // 파일 리스트 가져오기
            List<FileDto> fileList = fileAnnounceDao.selectFileListByAnoAdmin(announceDto.getAno());

            // 파일 삭제
            baseMultiFileUploader.fileListDelete(fileList, true);

            // 게시글 삭제
            if (announceDao.deleteAnnounce(announceDto) != 1) {
                throw new InvalidAnnounceException(ErrorType.ANNOUNCE_NOT_DELETED);
            }

            // 파일 db 삭제
            fileAnnounceDao.deleteFileListByAnoAdmin(announceDto.getAno());

            // 이미지 삭제
            Set<FileDto> fileSet = fileCkEditorDao.selectFileDto(announceDto.getPostId());
            ckEditorFileService.deleteFileSetFromServerNDb(fileSet);

        } catch (InvalidFileException e) {
            log.error("InvalidFileException mode: ", e);
            throw e;
        } catch (DataAccessException e) {
            log.error("DataAccessException mode: ", e);
            throw new InvalidAnnounceException(ErrorType.ANNOUNCE_NOT_DELETED);
        } catch (Exception e) {
            log.error("Exception mode: ", e);
            throw new InvalidAnnounceException(ErrorType.ANNOUNCE_NOT_DELETED);
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanUnusedImages() throws IOException {
        ckEditorFileService.cleanUnusedImages();
    }

    @Override
    public int countAnnounceUser(SearchCondition sc) {
        return announceDao.countAnnounceUser(sc);
    }

    @Override
    public List<AnnounceDto> getAnnouncePageUser(SearchCondition sc) {
        return announceDao.getAnnouncePageUser(sc);
    }

    @Override
    public AnnounceDto selectAnnounceUser(int ano) {
        return announceDao.selectAnnounceUser(ano);
    }

    @Override
    public List<AnnounceDto> selectAnnounceListByPostedAt(SearchCondition sc) {
        return announceDao.selectAnnounceListByPostedAt(sc);
    }

    @Override
    public List<AnnounceDto> selectAnnounceScheduleListByPostedAt() {
        return announceDao.selectAnnounceScheduleListByPostedAt();
    }
}
