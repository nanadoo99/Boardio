package com.nki.t1.service;

import com.nki.t1.domain.CkEditorPost;
import com.nki.t1.dto.FileDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface CkEditorFileService {

    FileDto upload(MultipartFile multipartFile) throws IOException;
    /*✅
        - 업로드 이후 db 저장
        ck: o
        db:  o
        aws: x
     */
    void saveImage(Object obj);

    /*✅
        - CkEditor로 작성된 글 수정시, 지워진 파일 삭제 & 추가된 파일 저장
        ck: o
        db: o
        aws: x
     */
    void fileSyncForPostUpdate(CkEditorPost newPost) throws IOException;

    /*✅
    - 이미지 삭제 - 서버 및 db
    - 게시물 삭제에 따른 이미지 전체 삭제
    ck: o
    db: o
    aws: o
 */
    @Transactional
    void deleteFileSetFromServerNDb(Set<FileDto> fileSet) throws IOException;


// ==== 가비지 이미지 처리 ====

    //    // 이미지 삭제
    /*
        - db 와 서버를 비교해, 서버에만 있고 기한이 만료된(수정중인 경우를 배제하기 위해) 이미지 삭제
        - 작성 중 저장을 하지 않은 경우, expirationPeriodSec 를 초과 했을때 삭제.
        ck: o
        db: o
        aws: o
     */
    void cleanUnusedImages() throws IOException ;
}
