package com.nki.t1.service;

import com.nki.t1.component.AbstractS3CkEditorFileUploader;
import com.nki.t1.dao.FileCkEditorDaoPostImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ckEditorFileServicePostImpl")
public class CkEditorFileServicePostImpl extends AbstractCkEditorFileService {

    public CkEditorFileServicePostImpl(FileCkEditorDaoPostImpl fileCkEditorDaoPostImpl,
                                       @Qualifier("s3CkEditorFileUploaderPost") AbstractS3CkEditorFileUploader s3CkEditorFileUploader) {
        super(fileCkEditorDaoPostImpl, s3CkEditorFileUploader, 7 * 24 * 60 * 60L);
    }

}
