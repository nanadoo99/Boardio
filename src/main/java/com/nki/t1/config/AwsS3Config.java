package com.nki.t1.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
    @Value("${aws.s3.accesskey}")
    private String accessKey;

    @Value("${aws.s3.secretekey}")
    private String secretKey;

    @Bean
    public BasicAWSCredentials AwsCredentials() {
        BasicAWSCredentials AwsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AwsCreds;
    }

    @Bean
    public AmazonS3 AwsS3Client() {
        AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentials()))
                .build();

        return s3Builder;
    }
}
