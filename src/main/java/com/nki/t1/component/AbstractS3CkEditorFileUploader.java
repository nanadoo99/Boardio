package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.AwsS3Utils;
import com.nki.t1.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

// ZoldFileUtilsCkEditorAWS 참고
@Slf4j
public abstract class AbstractS3CkEditorFileUploader implements CkEditorFileUploader{
    private final AwsS3Utils awsS3Utils;

    protected AbstractS3CkEditorFileUploader(AwsS3Utils awsS3Utils) {
        this.awsS3Utils = awsS3Utils;
    }

    // CkEditor의 에러 처리에 맞게 기본 IOException 사용
    @Override
    public FileDto upload(MultipartFile file) throws IOException {
//    public void upload(FileDto fileDto) throws IOException {
        log.info("FileUtils fileUploadCkEditor");

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

    // 이미지 삭제 - 서버
    @Override
    public void deleteFileSetFromServer(Set<FileDto> fileSet) throws IOException {
        log.info("FileUtils deleteFileSetFromServerNDb");
        for (FileDto fileDto : fileSet) {
            delete(fileDto, false);
        }
    }

    @Override
    public void deleteExpiredFileSetFromServer(Set<String> dbFileNameSet, long expirationPeriodSec) {
        Set<String> serverFileSet = awsS3Utils.getExpiredFilesFromS3(expirationPeriodSec);

        // 서버이미지가 db set에 없으면 삭제
        for (String serverFile : serverFileSet) {
            if (!dbFileNameSet.contains(serverFile)) {
                awsS3Utils.deleteObject(serverFile);
            }
        }
    }

}
