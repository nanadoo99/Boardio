package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context_test.xml"})
public class PostDaoImplTest {

    @Autowired
    private PostDao postDao;

    @Test
    public void insertPost() {
        for(int i=1 ; i<=200 ; i++){
            PostDto postDto = new PostDto(i % 3 == 0 ? 1 : 2, "title"+i, "content"+i);
            postDao.insertPost(postDto);
        }
    }
}