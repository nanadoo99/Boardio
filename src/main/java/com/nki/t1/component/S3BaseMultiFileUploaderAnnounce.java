package com.nki.t1.component;

import com.nki.t1.utils.AwsS3UtilsAnnounce;
import com.nki.t1.utils.SizeParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 파일 업로더 구현체 - 저장소: S3, 업로드 방식: 일반, 개수: 다중
@Service("s3BaseMultiFileUploaderAnnounce")
public class S3BaseMultiFileUploaderAnnounce extends AbstractS3BaseMultiFileUploader {
    public S3BaseMultiFileUploaderAnnounce(AwsS3UtilsAnnounce awsS3Utils,
                                           @Value("#{path['file.total.size.announce']}")
                                           String maxRequestSize,
                                           @Value("#{path['file.size.announce']}")
                                           String maxFileSize) {
        super(awsS3Utils, SizeParser.stringToLong(maxRequestSize), SizeParser.stringToLong(maxFileSize));
    }
}
