package com.nki.t1.old;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.FileDto;
import com.nki.t1.exception.InvalidFileException;
import com.nki.t1.utils.SizeParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

// 절대경로 사용예제.
// FileUtilsCkEditor
// UserPostController2
// PostServiceImpl2

// AbstractLocalBaseFileUploader 이동
@Slf4j
public class FileUtilsGeneral {

    private final String uploadDir;
    private final String backupDir;
    private final Long MAX_FILE_SIZE_PARESED;
    private final Long MAX_REQUEST_SIZE_PARESED;
    private final String MAX_FILE_SIZE;
    private final String MAX_REQUEST_SIZE;

    public FileUtilsGeneral(String uploadDir, String backupDir, String maxRequestSize, String maxFileSize) {
        this.uploadDir = uploadDir;
        this.backupDir = backupDir;
        MAX_REQUEST_SIZE = maxRequestSize;
        MAX_REQUEST_SIZE_PARESED = SizeParser.stringToLong(maxRequestSize);
        MAX_FILE_SIZE = maxFileSize;
        MAX_FILE_SIZE_PARESED = SizeParser.stringToLong(maxFileSize);
    }

// ===== 파일 업로드 =====
    // 다중 파일 업로드
// ✅
    public List<FileDto> fileListUpload(List<MultipartFile> multipartFiles) throws IOException {
        log.info("FileUtils fileUploadCkEditor");
        requestSizeCheck(multipartFiles);

        List<FileDto> uploadedFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                uploadedFiles.add(upload(multipartFile));
            }
        }
        return uploadedFiles;

    }

    // 단일 파일 업로드
    // ✅
    public FileDto fileUpload(MultipartFile multipartFile) throws IOException {
            fileSizeCheck(multipartFile);
            return upload(multipartFile);

    }

    // ✅
    // 파일 서버에 업로드 > 업로드 정보 반환(원래 이름, 업로드된 이름, 경로, 크기)
    public FileDto upload(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            checkUploadPath();
            String fileOrgNm = multipartFile.getOriginalFilename();
            String fileUploadedNm = getUploadedNm(fileOrgNm);
            multipartFile.transferTo(new File(uploadDir + fileUploadedNm));

            FileDto fileDto = new FileDto();
            fileDto.setFileOrgNm(fileOrgNm);
            fileDto.setFileUidNm(fileUploadedNm);
            fileDto.setUploadPath(uploadDir);
            fileDto.setSize(multipartFile.getSize());

            return fileDto;
        }

        throw new InvalidFileException(ErrorType.FILE_IS_EMPTY);
    }

// ===== 파일 삭제 =====
// ✅
    // 다중 파일 삭제
    public void fileListDelete(List<FileDto> uploadedFiles, boolean backup) throws IOException {
        if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
            log.info("파일 삭제 시작 - 총 {}개의 파일", uploadedFiles.size());
            for (FileDto fileDto : uploadedFiles) {
                log.info("파일 삭제 처리 중 - 파일명: {}, 경로: {}", fileDto.getFileOrgNm(), fileDto.getUploadFullPath());
                fileDelete(fileDto, backup);
            }   
            log.info("파일 삭제 완료 - 총 {}개의 파일", uploadedFiles.size());
        } else {
        log.info("삭제할 파일이 없습니다.");
        }
    }

    // ✅
    // 단일 파일 삭제
    public void fileDelete(FileDto fileDto, boolean backup) throws IOException {
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
            File backupPath = new File(backupDir);
            if (!file.exists()) {
                backupPath.mkdirs();
                log.info("백업 디렉토리 생성 - 경로: {}", backupDir);
            }

            backupFile = new File(backupPath + fileDto.getFileUidNm());
            Files.copy(file.toPath(), backupFile.toPath());
            log.info("파일 백업 완료 - 백업 경로: {}", backupFile.getPath());
        }

        // 파일 삭제 시도
        if (!file.delete()) {
            log.error("파일 삭제 실패 - 경로: {}", fullPath);
            if (backup && backupFile != null) {
                Files.move(backupFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                log.info("백업 파일 복원 완료 - 경로: {}", fullPath);
            }
            throw new InvalidFileException(ErrorType.FILE_NOT_DELETED);
        }
        log.info("파일 삭제 완료 - 경로: {}", fullPath);
        // 백업 파일 삭제
        if (backup && backupFile != null) {
            backupFile.delete();
            log.info("백업 파일 삭제 완료 - 백업 경로: {}", backupFile.getPath());
        }

    }

// ===== 파일 이름 =====
    // ✅
    private String getUploadedNm(String fileOrgNm) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + "_" + fileOrgNm;
    }


// ==== 경로 ====
    // 경로 유무 체크 및 생성
// ✅
    private void checkUploadPath() {
        // 실제 파일 저장 경로
        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
    }

// ===== 크기 =====
    // 전체 크기 체크
// ✅
    private void requestSizeCheck(List<MultipartFile> files) {

        long requestSize = files.stream().mapToLong(MultipartFile::getSize).sum();

        if(requestSize > MAX_REQUEST_SIZE_PARESED) {
            throw new InvalidFileException(ErrorType.FILE_TOTAL_SIZE_EXCEEDED, "최대" + MAX_REQUEST_SIZE + "까지 허용됩니다.");
        }

        for(MultipartFile file : files) {
            fileSizeCheck(file);
        }

    }

    // 단일 크기 체크
    // ✅
    private void fileSizeCheck(MultipartFile file) {
        if(file.getSize() > MAX_FILE_SIZE_PARESED) {
            throw new InvalidFileException(ErrorType.FILE_SIZE_EXCEEDED, "최대" + MAX_FILE_SIZE + "까지 허용됩니다.");
        }

    }
}
