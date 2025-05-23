package com.nki.t1.utils;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("AwsS3UtilsAnnounceAttachment")
public class AwsS3UtilsAnnounceAttachment extends AwsS3Utils {
    public AwsS3UtilsAnnounceAttachment(AmazonS3 s3Client,
                                        @Value("${aws.s3.bucketname}") String bucketName,
                                        @Value("${file.upload.directory.announce.attachment.aws}") String fileDir) {
        super(s3Client, bucketName, fileDir);
    }
}