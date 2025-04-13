package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.PostsMapper.";

    @Override
    public List<PostDto> selectPage(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectPage", sc);
    }

    @Override
    public int countPost(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countPost", sc);
    }

    @Override
    public List<PostDto> selectMyPostPage(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectMyPostPage", sc);
    }

    @Override
    public int countMyPost(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countMyPost", sc);
    }

    @Override
    public List<PostDto> selectPostPageByAdmin(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectPostPageByAdmin", sc);
    }

    @Override
    public List<PostDto> selectPostPageByAdminUno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectPostPageByAdminUno", sc);
    }

    @Override
    public int countPostByAdmin(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countPostByAdmin", sc);
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
    public int insertPost(PostDto postDto) {
        return sqlSession.insert(namespace + "insertPost", postDto);
    }

    @Override
    public int updatePost(PostDto postDto) {
        return sqlSession.update(namespace + "updatePost", postDto);
    }

    @Override
    public int deletePost(PostDto postDto) {
        return sqlSession.update(namespace + "deletePost", postDto);
    }

    @Override
    public int countByCmtUno(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countByCmtUno", sc);
    }

    @Override
    public UserDto selectUserDtoByPno(Integer pno) {
        return sqlSession.selectOne(namespace + "selectUserDtoByPno", pno);
    }
}
