package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.AwsS3Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;

// ZoldFileUtilsCkEditorAWS 참고
@Slf4j
public abstract class AbstractS3CkEditorFileUploader extends AbstractS3FileUploader implements CkEditorFileUploader{
    private final AwsS3Utils awsS3Utils;

    protected AbstractS3CkEditorFileUploader(AwsS3Utils awsS3Utils) {
        super(awsS3Utils);
        this.awsS3Utils = awsS3Utils;
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
