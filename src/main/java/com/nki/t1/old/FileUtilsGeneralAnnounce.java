package com.nki.t1.old;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component("FileUtilsGeneralAnnounce")
public class FileUtilsGeneralAnnounce extends FileUtilsGeneral {

    public FileUtilsGeneralAnnounce(@Value("#{path['file.upload.directory.announce']}")
                                    String uploadDir,
                                    @Value("#{path['file.upload.directory.backup']}")
                                    String backupDir,
                                    @Value("#{path['file.total.size.announce']}")
                                    String maxReqeustSize,
                                    @Value("#{path['file.size.announce']}")
                                    String maxFileSize) {
        super(uploadDir, backupDir, maxReqeustSize, maxFileSize);
    }


}
