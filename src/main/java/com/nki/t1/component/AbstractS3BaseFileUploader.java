package com.nki.t1.component;

import com.nki.t1.utils.AwsS3Utils;
import lombok.extern.slf4j.Slf4j;

// ZoldFileUtilsGeneral 참고
// 일반 업로드 방식 + S3 저장
@Slf4j
public abstract class AbstractS3BaseFileUploader extends AbstractS3FileUploader{

    protected AbstractS3BaseFileUploader(AwsS3Utils awsS3Utils, long maxFileSize) {
        super(awsS3Utils, maxFileSize);
    }
}
