package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;
import java.util.Map;

public interface PostDao {
    List<PostDto> selectPage(SearchCondition sc);
    int insertPost(PostDto postDto);
    int countPost(SearchCondition sc);
    PostDto selectPost(Integer pno);
    int increaseView(Integer pno);
    int deletePost(Map map);
    int updatePost(PostDto postDto);
}
