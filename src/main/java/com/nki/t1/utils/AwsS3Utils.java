package com.nki.t1.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.FileDto;
import com.nki.t1.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class AwsS3Utils {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String fileDir;

    protected AwsS3Utils(AmazonS3 s3Client, String bucketName, String fileDir) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.fileDir = fileDir;
    }

    // 클라이언트 엔드 포인트 제공
    public String getEndPointPath() {
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileDir;
    }

    // 전체 S3 키반환
    private String getDecodedKeyName(String fileName) {
        return fileDir + "/" + URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    }

    private String getKeyName(String fileName) {
        return fileDir + "/" + fileName;
    }

    // 업로드
    public String uploadObject(FileDto fileDto) throws IOException {
        String keyName = getDecodedKeyName(fileDto.getFileUidNm());
        // 1. bucket name, 2. key : full path to the file 3. file : new File로 생성한 file instance
        // 2. PutObjectRequest로 구현 가능
        log.info("Uploading object " + keyName);
        s3Client.putObject(new PutObjectRequest(bucketName, keyName, fileDto.getFile().getInputStream(), null));

        return getDecodedS3Url(keyName);
    }

    private String getDecodedS3Url(String keyName) {
        // getUrl()은 실제 S3에 저장된 객체의 keyName을 기반으로 URL을 생성합니다.
        URL s3Url = s3Client.getUrl(bucketName, keyName);
        // URL의 path 부분 >> UTF-8 기준으로 디코딩
        String decodedPath = URLDecoder.decode(s3Url.getPath(), StandardCharsets.UTF_8);
        String decodedUrl = s3Url.getProtocol() + "://" + s3Url.getHost() + decodedPath;
        if (s3Url.getQuery() != null) {
            decodedUrl += "?" + s3Url.getQuery();
        }
        return decodedUrl;
    }

    // 삭제
    public void deleteObject( String storedFileName) {
        String keyName = getKeyName(storedFileName);
        log.info("Deleting object " + keyName);
        if(!s3Client.doesObjectExist(bucketName, keyName)) {
            log.debug("S3 Url: " + s3Client.getUrl(bucketName, keyName));
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND,  "파일명: " + storedFileName);
        }
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
    }

    // 다운로드
    public byte[] downloadObject(String uuidName) {
        System.out.println("start of s3 download");
        S3Object s3Object = s3Client.getObject(bucketName, uuidName);
        System.out.println("s3Object = " + s3Object);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            // e.printStackTrace();
            throw new IllegalStateException("aws s3 다운로드 error");
        }
    }

    // 만료 파일 반환
    public Set<String> getExpiredFilesFromS3(long expirationPeriodSec) {
        Set<String> expiredFileSet = new HashSet<>();

        // 현재 시간 (밀리초 단위)
        long currentTime = System.currentTimeMillis();

        // S3 객체 목록 요청
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(fileDir + "/");

        ListObjectsV2Result result = s3Client.listObjectsV2(request);

        // 날짜 포맷 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (S3ObjectSummary summary : result.getObjectSummaries()) {
            Date lastModifiedDate = summary.getLastModified();
            long lastModifiedTime = lastModifiedDate.getTime();
            boolean isExpired = currentTime - lastModifiedTime > (expirationPeriodSec * 1000L);

            // 로그 출력
            System.out.println("");
            System.out.print("----- file name: " + summary.getKey());
            System.out.print(" L_____ lastModified: " + sdf.format(lastModifiedDate)
                    + " / timeDiff: " + (currentTime - lastModifiedTime)
                    + " / isExpired: " + isExpired);

            if (isExpired) {
                String filePath = summary.getKey();
                if (!filePath.endsWith("/")) { // 폴더가 아닌 파일만 추가
                    expiredFileSet.add(filePath.substring(filePath.lastIndexOf("/") + 1));
                }
            }
        }

        System.out.println("Expired files: " + expiredFileSet);
        return expiredFileSet;
    }
}