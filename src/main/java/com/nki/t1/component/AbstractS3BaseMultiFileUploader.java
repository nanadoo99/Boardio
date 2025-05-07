package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.AwsS3Utils;
import com.nki.t1.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// ZoldFileUtilsGeneral 참고
// 일반 업로드 방식 + S3 저장
// 사용처: AnnounceServiceImpl 공지사항 내 첨부파일
@Slf4j
public abstract class AbstractS3BaseMultiFileUploader extends AbstractS3FileUploader implements MultiFileUploader {
    private final long MAX_REQUEST_SIZE;
    private final long MAX_FILE_SIZE;

    protected AbstractS3BaseMultiFileUploader(AwsS3Utils awsS3Utils, long maxRequestSize, long maxFileSize) {
        super(awsS3Utils);
        this.MAX_REQUEST_SIZE = maxRequestSize;
        this.MAX_FILE_SIZE = maxFileSize;
    }

    @Override
    public List<FileDto> fileListUpload(List<MultipartFile> files) throws IOException {
        FileUtils.requestSizeCheck(files, MAX_REQUEST_SIZE, MAX_FILE_SIZE);

        List<FileDto> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                uploadedFiles.add(upload(file));
            }
        }
        return uploadedFiles;
    }

    @Override
    public void fileListDelete(List<FileDto> uploadedFiles, boolean backup) throws IOException {
        if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
            log.info("파일 삭제 시작 - 총 {}개의 파일", uploadedFiles.size());
            for (FileDto fileDto : uploadedFiles) {
                log.info("파일 삭제 처리 중 - 파일명: {}, 경로: {}", fileDto.getFileOrgNm(), fileDto.getUploadFullPath());
                delete(fileDto, backup);
            }
            log.info("파일 삭제 완료 - 총 {}개의 파일", uploadedFiles.size());
        } else {
            log.info("삭제할 파일이 없습니다.");
        }

    }
}
