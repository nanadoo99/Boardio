package com.nki.t1.component;

import com.nki.t1.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MultiFileUploader {
    List<FileDto> fileListUpload(List<MultipartFile> multipartFiles) throws IOException;
    void fileListDelete(List<FileDto> uploadedFiles, boolean backup) throws IOException;
}
