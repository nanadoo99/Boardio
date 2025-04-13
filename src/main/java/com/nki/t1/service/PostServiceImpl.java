package com.nki.t1.service;

import com.nki.t1.dao.FileCkEditorDao;
import com.nki.t1.dao.FileCkEditorDaoPostImpl;
import com.nki.t1.dao.PostDao;
import com.nki.t1.domain.ContentState;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.*;
import com.nki.t1.exception.InvalidCommentException;
import com.nki.t1.exception.InvalidPostException;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;


// 절대경로 사용예제.
// ckEditorFileService
// UserPostController2
// PostServiceImpl2

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final FileCkEditorDao fileCkEditorDao;

    private final CkEditorFileService ckEditorFileService;
    private final SessionUtils sessionUtils;

    //    public PostServiceImpl2(PostDao postDao, @Qualifier("FileDaoPost") FileCkEditorDao fileCkEditorDao, ckEditorFileService ckEditorFileService, SessionUtils sessionUtils) {
    public PostServiceImpl(PostDao postDao,
                           FileCkEditorDaoPostImpl fileCkEditorDao,
                           SessionUtils sessionUtils,
                           @Qualifier("ckEditorFileServicePostImpl")
                            CkEditorFileService ckEditorFileService) {
        this.postDao = postDao;
        this.ckEditorFileService = ckEditorFileService;
        this.fileCkEditorDao = fileCkEditorDao;
        this.sessionUtils = sessionUtils;
    }

    @Override
    public int getPostCount(SearchCondition sc) {
        return postDao.countPost(sc);
    }

    @Override
    public List<PostDto> getPostPage(SearchCondition sc) {
        List<PostDto> list = postDao.selectPage(sc);
        return list;
    }

    @Override
    public int countMyPost(SearchCondition sc) {
        return postDao.countMyPost(sc);
    }

    @Override
    public List<PostDto> selectMyPostPage(SearchCondition sc) {
        return postDao.selectMyPostPage(sc);
    }

    @Override
    public int countPostByAdmin(SearchCondition sc) {
        return postDao.countPostByAdmin(sc);
    }

    @Override
    public List<PostDto> selectPostPageByAdmin(SearchCondition sc) {
        return postDao.selectPostPageByAdmin(sc);
    }

    @Override
    public List<PostDto> selectPostPageByAdminUno(SearchCondition sc) {
        return postDao.selectPostPageByAdminUno(sc);
    }

    // 게시글 선택
    @Override
    public PostDto getPostWithAuth(Integer pno) {

//        UserDto userDto = sessionUtils.getUserDto();
        PostDto postDto = getExistPost(pno);

        log.info("Principal uno: " + ((UserSecurityDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        log.info("PostDto: " + postDto);

        sessionUtils.checkAdminOrWriter(postDto);
//        postAuth(userDto, PostDto);
        return postDto;
    }


    // 게시글 읽기
    // 일반 게시판에서 읽기
    @Override
    public PostDto readPost(Integer pno) {
        PostDto postDto = getExistPost(pno);

        if (postDto.getContentState() == ContentState.BLOCKED) throw new InvalidPostException(ErrorType.POST_BLOCKED);
        postDao.increaseView(pno);

        return postDto;
    }

    // 게시글 작성 - 게시글 저장
    @Transactional
    @Override
    public void savePost(PostDto postDto, HttpServletRequest request) throws InvalidPostException {
        try {

            // 검색을 위한 content 텍스트만을 추출
            postDto.extractContentTxtFromContent();

            // 게시물 저장 및 삭제
            if(postDao.insertPost(postDto) != 1) {
                throw new InvalidPostException(ErrorType.POST_NOT_CREATED);
            }
            // 이미지 db 저장
            ckEditorFileService.saveImage(postDto);

        } catch (DataAccessException e) {
            log.error("DataAccessException mode: ", e);
            throw new InvalidPostException(ErrorType.POST_NOT_CREATED, postDto);
        } catch (InvalidPostException e) {
            log.error("InvalidPostException : ", e);
            e.addObject(postDto);
            throw e;
        } catch (Exception e) {
            log.error("Exception mode: ", e);
            throw new InvalidPostException(ErrorType.POST_NOT_CREATED, postDto);
        }
    }

    // 게시글 작성 - 이미지 - 서버 저장
    @Override
    public FileDto ckFileUpload(MultipartFile file) throws IOException {
        return ckEditorFileService.upload(file);
    }


    @Transactional
    @Override
    public void updatePost(PostDto postDto, HttpServletRequest request) throws InvalidPostException {

        try {
/*            UserDto userDto = sessionUtils.getUserDto(request);
            PostDto oldPost = getPostWithAuth(PostDto.getPno(), userDto);*/
            PostDto oldPost = getPostWithAuth(postDto.getPno());

            if (oldPost.getContentState() == ContentState.BLOCKED)
                throw new InvalidPostException(ErrorType.POST_BLOCKED);

            // 수정 중 삭제된 이미지 - db 및 서버에서 삭제 & 새로 추가된 이미지 - db 저장
            ckEditorFileService.fileSyncForPostUpdate(postDto);

            // ===== 게시글 업데이트
            postDto.extractContentTxtFromContent();

            if(postDao.updatePost(postDto)!=1) throw new InvalidPostException(ErrorType.POST_NOT_UPDATED);

        } catch (DataAccessException e) {
            log.error("DataAccessException while updating the post", e);
            throw new InvalidPostException(ErrorType.REQUEST_FAILED, postDto);
        } catch (InvalidPostException e) {
            log.error("InvalidPostException while updating the post", e);
            e.addObject(postDto);
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("Exception while updating the post", e);
            throw new InvalidPostException(ErrorType.REQUEST_FAILED, postDto);
        }

    }


    // 게시글 저장 혹은 삭제

    @Override
    // 게시글 삭제
    public void deletePost(int pno, HttpServletRequest request) throws InvalidPostException {
        try {

            // 삭제 권한(관리자 혹은 작성자) 및 게시글 존재 여부 확인
/*            UserDto userDto = sessionUtils.getUserDto(request);
            PostDto PostDto = getPostWithAuth(pno, userDto);*/
            UserDto userDto = sessionUtils.getUserDto();
            PostDto postDto = getPostWithAuth(pno);

            // 차단 게시글 - 삭제 불가.
            if (postDto.getContentState() == ContentState.BLOCKED)
                throw new InvalidPostException(ErrorType.POST_BLOCKED);

            // 게시물 삭제
            PostDto postDto1 = new PostDto();
            postDto1.setPno(pno);
            postDto1.setUno(userDto.getUno());

            if (postDao.deletePost(postDto1) != 1) throw new InvalidPostException(ErrorType.REQUEST_FAILED);

            // 이미지 삭제
            Set<FileDto> fileSet = fileCkEditorDao.selectFileDto(postDto.getPno());
            ckEditorFileService.deleteFileSetFromServerNDb(fileSet);

        } catch (DataAccessException e) {
            log.error("DataAccessException while deleting post", e);
            throw new InvalidPostException(ErrorType.REQUEST_FAILED);
        } catch (InvalidPostException e) {
            log.error("InvalidPostException while deleting post", e);
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidPostException(ErrorType.REQUEST_FAILED);
        }
    }

    @Override
    public int countByCmtUno(SearchCondition sc) throws InvalidCommentException {
        try {
            return postDao.countByCmtUno(sc);
        } catch (DataAccessException e) {
            log.error("Database error occurred while counting comments", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error occurred while counting comments", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_FOUND);
        }
    }

    // 유효한 게시글 존재 여부 확인 및 반환
    private PostDto getExistPost(Integer pno) throws InvalidPostException {
        PostDto postDto = postDao.selectPost(pno);
        if (postDto == null || postDto.getPno() == null || postDto.getPno() == 0) {
            throw new InvalidPostException(ErrorType.POST_NOT_FOUND);
        } else if (postDto.isDel()) {
            throw new InvalidPostException(ErrorType.POST_DELETED);
        }
        return postDto;
    }

/*    private void postAuth(UserDto userDto, PostDto PostDto) throws InvalidPostException {
        // 사용자가 관리자거나, 게시글의 작성자일 때만 정상 작동.
        if (!(Role.ADMIN == userDto.getUserRole() || (PostDto.getUno() != null && PostDto.getUno().equals(userDto.getUno())))) {  // 검토: "ADMIN" >> value 로 변경
            throw new InvalidPostException(ErrorType.POST_UNAUTH);
        }
    }*/

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanUnusedCkEditorImages() throws IOException {
        ckEditorFileService.cleanUnusedImages();
    }
}
