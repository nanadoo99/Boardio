package com.nki.t1.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component("FileUtilsGeneralBanner")
public class FileUtilsGeneralBanner extends FileUtilsGeneral {

    public FileUtilsGeneralBanner(@Value("#{path['file.upload.directory.banner']}")
                                    String uploadDir,
                                  @Value("#{path['file.upload.directory.backup']}")
                                    String backupDir,
                                  @Value("#{path['file.size.banner']}")
                                  String maxRequestSize,
                                  @Value("#{path['file.size.banner']}")
                                  String maxFileSize) {
        super(uploadDir, backupDir, maxRequestSize, maxFileSize);
    }

}
