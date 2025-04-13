package com.nki.t1.utils;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("AwsS3UtilsPost")
public class AwsS3UtilsPost extends AwsS3Utils {
    public AwsS3UtilsPost(AmazonS3 s3Client,
                          @Value("${aws.s3.bucketname}") String bucketName,
                          @Value("${file.upload.directory.post.ckEditor.aws}") String fileDir) {
        super(s3Client, bucketName, fileDir);
    }
}