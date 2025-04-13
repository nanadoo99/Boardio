package com.nki.t1.utils;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.exception.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {

    // UUID 생성
    public static String getUploadedName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uploadedName = uuid + "_" + URLDecoder.decode(Objects.requireNonNull(file.getOriginalFilename()), StandardCharsets.UTF_8);
        System.out.println("uploadedName = " + uploadedName);
        return uploadedName;
    }

    // 확장자 추출
    public static String extractExt(String fileOrgNm) {
        return fileOrgNm.substring(fileOrgNm.lastIndexOf(".") + 1);
    }

    // 전체 경로 중 파일명만을 추출
    public static String extractFileName(String fileFullPath) {
        String[] parts = fileFullPath.split("[/\\\\]");
        return parts[parts.length - 1];
    }

    // 크기 확인
    public static void fileSizeCheck(MultipartFile file, long maxFileSize) {
        if(file.getSize() > maxFileSize) {
            throw new InvalidFileException(ErrorType.FILE_SIZE_EXCEEDED, "[파일명] " + file.getOriginalFilename() + "(" + SizeParser.longToString(file.getSize()) + "), 최대" + SizeParser.longToString(maxFileSize) + "까지 허용됩니다.");
        }
    }

    // 전체 요청 크기 확인
    public static void requestSizeCheck(List<MultipartFile> files, long maxReqeustSize, long maxFileSize) {

        long requestSize = files.stream().mapToLong(MultipartFile::getSize).sum();

        if(requestSize > maxReqeustSize) {
            throw new InvalidFileException(ErrorType.FILE_TOTAL_SIZE_EXCEEDED,  "현재 요청 크기: " + SizeParser.longToString(requestSize) + ",최대" + SizeParser.longToString(maxReqeustSize) + "까지 허용됩니다.");
        }

        for(MultipartFile file : files) {
            fileSizeCheck(file, maxFileSize);
        }
    }

}
