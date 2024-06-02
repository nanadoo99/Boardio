package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PostDaoImpl implements PostDao {
    @Autowired
    private SqlSession sqlSession;
    private String namespace = "com.nki.t1.dao.PostsMapper.";

    @Override
    public List<PostDto> selectPage(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectPage", sc);
    }

    @Override
    public int insertPost(PostDto postDto) {
        return sqlSession.insert(namespace + "insertPost", postDto);
    }

    @Override
    public int countPost(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countPost", sc);
    }

    @Override
    public PostDto selectPost(Integer pno) {
        return sqlSession.selectOne(namespace + "selectPost", pno);
    }

    @Override
    public int increaseView(Integer pno) {
        return sqlSession.update(namespace + "increaseView", pno);
    }

    @Override
    public int updatePost(PostDto postDto) {
        return sqlSession.update(namespace + "updatePost", postDto);
    }

    @Override
    public int deletePost(Map map) {
        return sqlSession.update(namespace + "deletePost", map);
    }
}
