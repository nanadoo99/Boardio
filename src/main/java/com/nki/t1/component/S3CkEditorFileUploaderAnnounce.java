package com.nki.t1.component;

import com.nki.t1.utils.AwsS3UtilsAnnounceCkEditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 파일 업로더 구현체(공지사항) - 저장소: S3, 업로드 방식: CkEditor, 개수: 단일
@Slf4j
@Component("s3CkEditorFileUploaderAnnounce")
public class S3CkEditorFileUploaderAnnounce extends AbstractS3CkEditorFileUploader{

    public S3CkEditorFileUploaderAnnounce(AwsS3UtilsAnnounceCkEditor awsS3UtilsAnnounceCkEditor) {
        super(awsS3UtilsAnnounceCkEditor);
    }

}
