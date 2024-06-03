package com.nki.t1.service;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

public interface PostService{
    List<PostDto> getPage(SearchCondition sc);
    int getPostCount(SearchCondition sc);
    PostDto getPost(Integer pno);

    int deletePost(Map map);
    int createPost(PostDto postDto, MultipartHttpServletRequest mpRequest);
    int updatePost(PostDto postDto);
}
