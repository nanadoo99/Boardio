package com.nki.t1.component;

import com.nki.t1.utils.AwsS3UtilsBanner;
import com.nki.t1.utils.SizeParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 파일 업로더 구현체 - 저장소: S3, 업로드 방식: 일반, 개수: 단일
@Component("S3BaseFileUploaderBanner")
public class S3BaseFileUploaderBanner extends AbstractS3BaseFileUploader {
    public S3BaseFileUploaderBanner(AwsS3UtilsBanner awsS3Utils,
                                         @Value("#{path['file.size.announce']}")
                                           String maxFileSize) {
        super(awsS3Utils, SizeParser.stringToLong(maxFileSize));
    }
}
