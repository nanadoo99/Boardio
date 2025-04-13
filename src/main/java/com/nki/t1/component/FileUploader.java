package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    FileDto upload(MultipartFile multipartFile) throws IOException;
    void delete(FileDto fileDto, boolean backup) throws IOException;
}
