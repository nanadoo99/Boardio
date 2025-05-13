package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.AwsS3Utils;
import com.nki.t1.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// AbstractS3CkEditorFileUploader 참고
// 일반 업로드 방식 + S3 저장
@Slf4j
public abstract class AbstractS3FileUploader implements FileUploader {
    private final AwsS3Utils awsS3Utils;
    private final long MAX_FILE_SIZE;

    // 파일 사이즈를 체크한다.
    protected AbstractS3FileUploader(AwsS3Utils awsS3Utils, long maxFileSize) {
        this.awsS3Utils = awsS3Utils;
        this.MAX_FILE_SIZE = maxFileSize;
    }

    // 파일 사이즈를 체크하지 않는다.
    protected AbstractS3FileUploader(AwsS3Utils awsS3Utils) {
        this.awsS3Utils = awsS3Utils;
        this.MAX_FILE_SIZE = 0;
    }

    @Override
    public FileDto upload(MultipartFile file) throws IOException {
        if (MAX_FILE_SIZE > 0) {
            FileUtils.fileSizeCheck(file, MAX_FILE_SIZE);
        }
        FileDto fileDto = new FileDto();

        fileDto.setFile(file);
        fileDto.setFileOrgNm(file.getOriginalFilename());
        fileDto.setFileUidNm(FileUtils.getUploadedName(file));
        String endPointUrl = awsS3Utils.uploadObject(fileDto);
        fileDto.setWebPath(endPointUrl);

        return fileDto;
    }

    @Override
    public void delete(FileDto fileDto, boolean backup) throws IOException {
        awsS3Utils.deleteObject(fileDto.getFileUidNm());
    }

}
