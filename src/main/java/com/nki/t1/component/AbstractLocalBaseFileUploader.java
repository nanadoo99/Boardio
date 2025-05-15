/*
package com.nki.t1.component;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.FileDto;
import com.nki.t1.exception.InvalidFileException;
import com.nki.t1.utils.FileUtils;
import com.nki.t1.utils.LocalFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

// ZoldFileUtilsGeneral 참고.
// 일반 업로드 방식 + 운영 서버 저장
@Slf4j
public abstract class AbstractLocalBaseFileUploader implements FileUploader {

    private final String uploadDir;
    private final String backupDir;
    private final long MAX_REQUEST_SIZE;
    private final long MAX_FILE_SIZE;

    public AbstractLocalBaseFileUploader(String uploadDir, String backupDir, long maxRequestSize, long maxFileSize) {
        this.uploadDir = uploadDir;
        this.backupDir = backupDir;
        this.MAX_REQUEST_SIZE = maxRequestSize;
        this.MAX_FILE_SIZE = maxFileSize;
    }

    // ===== 파일 업로드 =====
    // 단일 파일 업로드
    @Override
    public FileDto upload(MultipartFile file) throws IOException {
        FileUtils.fileSizeCheck(file, MAX_FILE_SIZE);
        return realUpload(file);
    }

    // 파일 서버에 업로드 > 업로드 정보 반환(원래 이름, 업로드된 이름, 경로, 크기)
    private FileDto realUpload(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            LocalFileUtils.checkUploadPath(uploadDir);
            String fileUploadedNm = FileUtils.getUploadedName(multipartFile);
            multipartFile.transferTo(new File(uploadDir + fileUploadedNm));

            FileDto fileDto = new FileDto();
            fileDto.setFileOrgNm(multipartFile.getOriginalFilename());
            fileDto.setFileUidNm(fileUploadedNm);
            fileDto.setUploadPath(uploadDir);
            fileDto.setSize(multipartFile.getSize());

            return fileDto;
        }

        throw new InvalidFileException(ErrorType.FILE_IS_EMPTY);
    }

    @Override
    public void delete(FileDto fileDto, boolean backup) throws IOException {
        String fullPath = fileDto.getUploadFullPath();
        File file = new File(fullPath);

        // 파일 존재 여부 확이
        if (!file.exists()) {
            log.error("파일을 찾을 수 없음 - 경로: {}", fullPath);
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND);
        }
        log.info("파일 삭제 시작 - 경로: {}", fullPath);
        // 파일 백업
        File backupFile = null;
        if (backup) {
            // 백업 디렉토리 생성 및 확인 (checkUploadPath 사용)
            LocalFileUtils.checkUploadPath(backupDir);
            // File 객체 생성 시 부모 디렉토리와 파일 이름을 명시적으로 분리
            File backupPath = new File(backupDir);
            backupFile = new File(backupPath, fileDto.getFileUidNm());

            try {
                Files.copy(file.toPath(), backupFile.toPath());
                log.info("파일 백업 완료 - 백업 경로: {}", backupFile.getPath());
            } catch (IOException e) {
                log.error("파일 백업 실패 - 경로: {}", backupFile.getPath(), e);
                throw e;
            }
        }

        // 파일 삭제 시도
        try {
            Files.delete(file.toPath());
            log.info("파일 삭제 완료 - 경로: {}", fullPath);
        } catch (IOException deleteException) {
            log.error("파일 삭제 실패 - 경로: {}", fullPath, deleteException);
            if (backup && backupFile.exists()) {
                try {
                    Files.move(backupFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log.info("백업 파일 복원 완료 - 경로: {}", fullPath);
                } catch (IOException restoreException) {
                    log.error("백업 파일 복원 실패 - 경로: {}", fullPath, restoreException);
                }
            }
            throw new InvalidFileException(ErrorType.FILE_NOT_DELETED);
        }

        // 백업 파일 삭제
        if (backup && backupFile.exists()) {
            if (backupFile.delete()) {
                log.info("백업 파일 삭제 완료 - 백업 경로: {}", backupFile.getPath());
            } else {
                log.warn("백업 파일 삭제 실패 - 백업 경로: {}", backupFile.getPath());
            }
        }

    }

}
*/
