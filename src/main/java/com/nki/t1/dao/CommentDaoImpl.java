package com.nki.t1.dao;

import com.nki.t1.dto.CommentDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private SqlSession sqlSession;
    private String namespace = "com.nki.t1.dao.CommentsMapper.";

    @Override
    public int countComments(int pno) {
        return sqlSession.selectOne(namespace + "countComments", pno);
    }

    @Override
    public List<CommentDto> selectComments(int pno) {
        return sqlSession.selectList(namespace + "selectComments", pno);
    }

    @Override
    public int insertComment(CommentDto commentDto) {
        return sqlSession.insert(namespace + "insertComment", commentDto);
    }

    @Override
    public int updateComment(CommentDto commentDto) {
        return sqlSession.update(namespace + "updateComment", commentDto);
    }

    @Override
    public int deleteComment(Map map) {
        return sqlSession.delete(namespace + "deleteComment", map);
    }

    @Override
    public int deleteAll(int pno) {
        return sqlSession.delete(namespace + "deleteAll", pno);
    }

}
