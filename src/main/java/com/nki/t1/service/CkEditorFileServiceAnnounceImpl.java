package com.nki.t1.service;

import com.nki.t1.component.CkEditorFileUploader;
import com.nki.t1.dao.FileCkEditorDao;
import com.nki.t1.dao.FileCkEditorDaoAnnounceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ckEditorFileServiceAnnounceImpl")
public class CkEditorFileServiceAnnounceImpl extends AbstractCkEditorFileService {

    public CkEditorFileServiceAnnounceImpl(@Qualifier("fileCkEditorDaoAnnounceImpl") FileCkEditorDao fileCkEditorDao,
                                           @Qualifier("s3CkEditorFileUploaderAnnounce") CkEditorFileUploader s3CkEditorFileUploader) {
        super(fileCkEditorDao, s3CkEditorFileUploader, 7 * 24 * 60 * 60L);
    }

}
