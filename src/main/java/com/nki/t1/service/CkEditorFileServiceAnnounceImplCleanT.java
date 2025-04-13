package com.nki.t1.service;

import com.nki.t1.component.CkEditorFileUploader;
import com.nki.t1.dao.FileCkEditorDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ckEditorFileServiceAnnounceImplCleanTest")
public class CkEditorFileServiceAnnounceImplCleanT extends AbstractCkEditorFileService {

    public CkEditorFileServiceAnnounceImplCleanT(@Qualifier("fileCkEditorDaoAnnounceImpl") FileCkEditorDao fileCkEditorDao,
                                                 @Qualifier("s3CkEditorFileUploaderAnnounce") CkEditorFileUploader s3CkEditorFileUploader) {
        super(fileCkEditorDao, s3CkEditorFileUploader, 60L);
    }

}
