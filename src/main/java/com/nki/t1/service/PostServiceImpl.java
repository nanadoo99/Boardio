package com.nki.t1.service;

import com.nki.t1.dao.PostDao;
import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostDao postDao;

    @Override
    public List<PostDto> getPage(SearchCondition sc) {
        return postDao.selectPage(sc);
    }

    @Override
    public int getPostCount(SearchCondition sc) {
        return postDao.countPost(sc);
    }

    @Override
    public PostDto getPost(Integer pno) {
        postDao.increaseView(pno);
        return postDao.selectPost(pno);
    }

    @Override
    public int createPost(PostDto postDto) {
        return postDao.insertPost(postDto);
    }

    @Override
    public int updatePost(PostDto postDto) {
        return postDao.updatePost(postDto);
    }

    @Override
    public int deletePost(Map map) {
        return postDao.deletePost(map);
    }
}
