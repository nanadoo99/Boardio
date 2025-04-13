package com.nki.t1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

// 사용자 페이지 - 밴너
@Slf4j
@RestController
@RequestMapping("/public/banners")
public class PublicBannerController {

    @Value("#{path['file.upload.directory.banner']}")
    private String uploadaBannerPath;

    // 배너 이미지
    @GetMapping("/{fileUidNm:.+}") // @PathVariable에 확장자까지 포함.
    public ResponseEntity<Resource> getBannerImage(@PathVariable("fileUidNm") String fileUidNm, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        log.info("getBannerImage fileUidNm={}", fileUidNm);
        // 이미지 경로 (fileUidNm으로 파일을 찾음)
        Path imagePath = Paths.get(uploadaBannerPath, fileUidNm);
        Resource imageResource = new UrlResource(imagePath.toUri());

        // 파일 내용으로 해시 값 생성 (ETag)
        String eTag = generateETag(imagePath);

        // 클라이언트가 보낸 If-None-Match 값 확인
        String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH);

        // If-None-Match 값이 ETag와 같다면, 변경되지 않았으므로 304 응답
        if (eTag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        // ETag 설정하여 이미지 리소스를 응답으로 반환
        return ResponseEntity.ok()
                .eTag(eTag)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))  // 1일 동안 캐시
                .contentType(checkMediaType(fileUidNm))
                .body(imageResource);
    }

    private MediaType checkMediaType(String fileUidNm) {
        String extension = fileUidNm.substring(fileUidNm.lastIndexOf(".")+1).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // 기본값 (바이너리 데이터)
        }
    }

    // 해시 값으로 ETag 생성 (SHA-256 사용)
    private String generateETag(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");  // 해시 알고리즘 선택
        byte[] fileBytes = Files.readAllBytes(filePath);
        byte[] hashBytes = digest.digest(fileBytes);
        StringBuilder sb = new StringBuilder();

        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));  // 바이트를 16진수로 변환
        }

        return sb.toString();
    }
}
