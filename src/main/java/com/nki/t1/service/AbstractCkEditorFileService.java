package com.nki.t1.service;

import com.nki.t1.domain.CkEditorPost;
import com.nki.t1.component.CkEditorFileUploader;
import com.nki.t1.dao.FileCkEditorDao;
import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.CkEditorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//S3CkEditorFileUploader의 구현체를 가져오고 외부에서는 Abstrac/tCkEditorFileService 만 호출한다.

@Slf4j
public abstract class AbstractCkEditorFileService implements CkEditorFileService {
    private final FileCkEditorDao fileCkEditorDao;
    public final CkEditorFileUploader ckEditorFileUploader;
    private final long expirationPeriodSec;

    protected AbstractCkEditorFileService(FileCkEditorDao fileCkEditorDao, CkEditorFileUploader ckEditorFileUploader, long expirationPeriodSec) {
        this.fileCkEditorDao = fileCkEditorDao;
        this.ckEditorFileUploader = ckEditorFileUploader;
        this.expirationPeriodSec = expirationPeriodSec;

    }

    @Override
    public FileDto upload(MultipartFile multipartFile) throws IOException {
        return ckEditorFileUploader.upload(multipartFile);
    }

    /*✅
        - 업로드 이후 db 저장
        ck: o
        db:  o
        aws: x
     */
    @Override
    public void saveImage(Object obj) {
        // 저장될 이미지 정보 추출 & postIdx 부여
        Set<FileDto> fileDtoSet = CkEditorUtils.setPostIdx(obj);


        // db 저장
        if (fileDtoSet != null && fileDtoSet.size() > 0) {
            for (FileDto fileDto : fileDtoSet) {
                fileCkEditorDao.insertFile(fileDto);
            }
        }
    }

    /*✅
        - CkEditor로 작성된 글 수정시, 지워진 파일 삭제 & 추가된 파일 저장
        ck: o
        db: o
        aws: x
     */
    @Override
    public void fileSyncForPostUpdate(CkEditorPost newPost) throws IOException {
        // db 에서 해당 게시물 이미지 set 가져오기
        Set<FileDto> dbUuids = fileCkEditorDao.selectFileDto(newPost.getPostId());

        // 새로운 게시글에서 uuid 추출
        Set<FileDto> contentUuids = CkEditorUtils.extractFileDto(newPost);

        // 1) db - 삭제된 기존 게시물
        Set<FileDto> removedSet = new HashSet<>(dbUuids);
        removedSet.removeAll(contentUuids);
        // - db 삭제처리
        for (FileDto removedFileDto : removedSet) {
            fileCkEditorDao.deleteFileByFileUuid(removedFileDto.getFileUidNm());
        }
        // - 서버 삭제처리
        if (!removedSet.isEmpty()) {
            ckEditorFileUploader.deleteFileSetFromServer(removedSet);
        }

        // 2) 게시글 - db : 새로추가
        Set<FileDto> addedSet = new HashSet<>(contentUuids);
        addedSet.removeAll(dbUuids);
        // - db 저장
        saveImage(addedSet);
    }

    /*✅
    - 이미지 삭제 - 서버 및 db
    - 게시물 삭제에 따른 이미지 전체 삭제
    ck: o
    db: o
    aws: o
 */
    @Override
    @Transactional
    public void deleteFileSetFromServerNDb(Set<FileDto> fileSet) throws IOException {
        if (fileSet != null && fileSet.size() > 0) {
            ckEditorFileUploader.deleteFileSetFromServer(fileSet);
            deleteFileSetFromDb(fileSet);
        }

    }

    // ✅
    private void deleteFileSetFromDb(Set<FileDto> fileSet){
        Iterator<FileDto> iterator = fileSet.iterator();
        if (iterator.hasNext()) {
            FileDto randomFile = iterator.next();
            System.out.println("randomFile pno = " + randomFile.getPostIdx());
            fileCkEditorDao.deleteFileByPostIdx(randomFile.getPostIdx());
        }

    }


// ==== 가비지 이미지 처리 ====

    //    // 이미지 삭제
    /*
        - db 와 서버를 비교해, 서버에만 있고 기한이 만료된(수정중인 경우를 배제하기 위해) 이미지 삭제
        - 작성 중 저장을 하지 않은 경우, expirationPeriodSec 를 초과 했을때 삭제.
        ck: o
        db: o
        aws: o
     */
    @Override
    public void cleanUnusedImages() throws IOException {
        log.info("FileUtils cleanUnusedImages");

        // db 정보
        Set<String> dbFileNameSet = fileCkEditorDao.selectAllUids(expirationPeriodSec);
        ckEditorFileUploader.deleteExpiredFileSetFromServer(dbFileNameSet, expirationPeriodSec);
    }
}
