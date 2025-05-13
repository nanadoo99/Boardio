package com.nki.t1.component;

import com.nki.t1.utils.AwsS3UtilsBanner;
import com.nki.t1.utils.SizeParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 파일 업로더 구현체 - 저장소: S3, 업로드 방식: 일반, 개수: 단일
@Service("S3BaseMultiFileUploaderBanner")
public class S3BaseMultiFileUploaderBanner extends AbstractS3BaseFileUploader {
    public S3BaseMultiFileUploaderBanner(AwsS3UtilsBanner awsS3Utils,
                                         @Value("#{path['file.size.announce']}")
                                           String maxFileSize) {
        super(awsS3Utils, SizeParser.stringToLong(maxFileSize));
    }
}
