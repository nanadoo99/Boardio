package com.nki.t1.component;

import com.nki.t1.utils.SizeParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// ZoldFileUtilsGeneralBanner 참고
@Slf4j
@Component("LocalBaseFileUploaderBanner")
public class LocalBaseFileUploaderBanner extends AbstractLocalBaseFileUploader {

    public LocalBaseFileUploaderBanner(@Value("#{path['file.upload.directory.banner']}")
                                       String uploadDir,
                                       @Value("#{path['file.upload.directory.backup']}")
                                       String backupDir,
                                       @Value("#{path['file.total.size.banner']}")
                                       String maxRequestSize,
                                       @Value("#{path['file.size.banner']}")
                                       String maxFileSize) {
        super(uploadDir, backupDir, SizeParser.stringToLong(maxRequestSize), SizeParser.stringToLong(maxFileSize));
    }

}
