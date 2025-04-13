package com.nki.t1.utils;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.exception.InvalidFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileUtils {

    // 경로 유무 체크 및 생성
    public static void checkUploadPath(String uploadDir) {
        // 실제 파일 저장 경로
        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new InvalidFileException(ErrorType.FILE_PATH_CREATION_FAILED, "경로: " + uploadDir);
            }
        }
    }

    // 경로 + 파일 유무 체크

    // 만료 파일 체크..?
}
