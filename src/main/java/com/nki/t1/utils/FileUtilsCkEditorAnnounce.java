package com.nki.t1.utils;

import com.nki.t1.dao.FileCkEditorDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtilsCkEditorAnnounce extends FileUtilsCkEditor {
    public FileUtilsCkEditorAnnounce(@Qualifier("fileCkEditorDaoAnnounceImpl") FileCkEditorDao fileCkEditorDao,
                                     @Value("#{path['file.upload.directory.announce.ckEditor']}")
                                 String uploadDir,
                                     @Value("#{path['file.web.directory.announce.ckEditor']}")
                                 String webDir) {
        super(fileCkEditorDao, uploadDir, webDir, 7 * 24 * 60 * 60L);
    }
}
