package com.nki.t1.service;

import com.nki.t1.dao.FileDao;
import com.nki.t1.dao.PostDao;
import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private FileUtils fileUtils;

    @Autowired
    PostDao postDao;

    @Autowired
    FileDao fileDao;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createPost(PostDto postDto, MultipartHttpServletRequest mpRequest) {
        int result = postDao.insertPost(postDto);

        if (result != 1) return result;

        try {
            List<Map<String, Object>> fileList = fileUtils.parseInsertFileInfo(postDto, mpRequest);
            for (Map<String, Object> file : fileList) {
                result = fileDao.insertFile(file);
                if (result != 1) {
                    throw new RuntimeException("File insert failed");
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
