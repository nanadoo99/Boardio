package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;

import java.util.List;

public interface PostDao {
    List<PostDto> selectPage(SearchCondition sc);
    int countPost(SearchCondition sc);
    List<PostDto> selectMyPostPage(SearchCondition sc);
    int countMyPost(SearchCondition sc);
    List<PostDto> selectPostPageByAdmin(SearchCondition sc);
    List<PostDto> selectPostPageByAdminUno(SearchCondition sc);
    int countPostByAdmin(SearchCondition sc);

    PostDto selectPost(Integer pno);
    int increaseView(Integer pno);
    int deletePost(PostDto postDto);
    int updatePost(PostDto postDto);
    int countByCmtUno(SearchCondition sc);

//    List<PostDto> getPageByCmtUno(SearchCondition sc);  삭제예정 0901
    int insertPost(PostDto postDto);
    UserDto selectUserDtoByPno(Integer pno);
}
