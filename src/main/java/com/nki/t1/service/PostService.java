package com.nki.t1.service;

import com.nki.t1.dto.*;
import com.nki.t1.exception.InvalidPostException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface PostService{

    List<PostDto> getPostPage(SearchCondition sc);
    int getPostCount(SearchCondition sc);
    List<PostDto> selectMyPostPage(SearchCondition sc);
    int countMyPost(SearchCondition sc);
    List<PostDto> selectPostPageByAdmin(SearchCondition sc);
    List<PostDto> selectPostPageByAdminUno(SearchCondition sc);
    int countPostByAdmin(SearchCondition sc);

    PostDto getPostWithAuth (Integer pno);
    PostDto readPost(Integer pno);

    void deletePost(int pno, HttpServletRequest request) throws InvalidPostException;
    void updatePost(PostDto postDto, HttpServletRequest request);
    void savePost(PostDto postDto, HttpServletRequest request);
    FileDto ckFileUpload(MultipartFile file) throws IOException;

    int countByCmtUno(SearchCondition sc);
    void cleanUnusedCkEditorImages() throws IOException;
//    List<PostDto> getPageByCmtUno(SearchCondition sc);  삭제예정 0901
}
