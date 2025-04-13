package com.nki.t1.service;

import com.nki.t1.component.S3CkEditorFileUploaderPost;
import com.nki.t1.dao.FileCkEditorDaoPostImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ckEditorFileServicePostImplCleanTest")
public class CkEditorFileServicePostImplCleanT extends AbstractCkEditorFileService {

    public CkEditorFileServicePostImplCleanT(FileCkEditorDaoPostImpl fileCkEditorDaoPostImpl,
                                             S3CkEditorFileUploaderPost S3CkEditorFileUploader) {
        super(fileCkEditorDaoPostImpl, S3CkEditorFileUploader, 60L);
    }

}
