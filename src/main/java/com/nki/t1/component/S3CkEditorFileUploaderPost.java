package com.nki.t1.component;

import com.nki.t1.utils.AwsS3UtilsPost;
import org.springframework.stereotype.Component;

// 파일 업로더 구현체(게시판) - 저장소: S3, 업로드 방식: CkEditor, 개수: 단일
@Component("s3CkEditorFileUploaderPost")
public class S3CkEditorFileUploaderPost extends AbstractS3CkEditorFileUploader{
    public S3CkEditorFileUploaderPost(AwsS3UtilsPost awsS3Utils) {
        super(awsS3Utils);
    }
}
