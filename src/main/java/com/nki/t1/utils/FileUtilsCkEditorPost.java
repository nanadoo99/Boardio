package com.nki.t1.utils;

import com.nki.t1.dao.FileCkEditorDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtilsCkEditorPost extends FileUtilsCkEditor {
    public FileUtilsCkEditorPost(@Qualifier("fileCkEditorDaoPostImpl") FileCkEditorDao fileCkEditorDao,
                                 @Value("#{path['file.upload.directory.post.ckEditor']}")
                                 String uploadDir,
                                 @Value("#{path['file.web.directory.post.ckEditor']}")
                                 String webDir) {
        super(fileCkEditorDao, uploadDir, webDir, 7 * 24 * 60 * 60L); // 일 시간 분 초
    }
}
