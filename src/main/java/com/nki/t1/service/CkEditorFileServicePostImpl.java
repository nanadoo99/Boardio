package com.nki.t1.service;

import com.nki.t1.component.AbstractS3CkEditorFileUploader;
import com.nki.t1.component.CkEditorFileUploader;
import com.nki.t1.component.S3CkEditorFileUploaderPost;
import com.nki.t1.dao.FileCkEditorDaoPostImpl;
import com.nki.t1.utils.AwsS3UtilsPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ckEditorFileServicePostImpl")
public class CkEditorFileServicePostImpl extends AbstractCkEditorFileService {

    public CkEditorFileServicePostImpl(FileCkEditorDaoPostImpl fileCkEditorDaoPostImpl,
                                       @Qualifier("s3CkEditorFileUploaderPost") CkEditorFileUploader s3CkEditorFileUploader) {
        super(fileCkEditorDaoPostImpl, s3CkEditorFileUploader, 7 * 24 * 60 * 60L);
    }

}
