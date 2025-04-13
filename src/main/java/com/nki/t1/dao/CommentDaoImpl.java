package com.nki.t1.dao;

import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.CommentsMapper.";

    @Override
    public int countCommentByAdmin(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countCommentByAdmin", sc);
    }

    @Override
    public List<CommentDto> selectCommentPageByAdmin(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectCommentPageByAdmin", sc);
    }

    @Override
    public List<CommentDto> selectCommentByAdminPno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectCommentByAdminPno", sc);
    }

    @Override
    public CommentDto selectCommentByCno(Integer cno) {
        return sqlSession.selectOne(namespace + "selectCommentByCno", cno);
    }

    @Override
    public List<CommentDto> selectComments(CommentDto commentDto) {
        return sqlSession.selectList(namespace + "selectComments", commentDto);
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

    @Override
    public int countCommentsByUno(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countCommentsByUno", sc);
    }

    @Override
    public List<CommentDto> getCommentPageByUno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getCommentPageByUno", sc);
    }

    @Override
    public List<CommentDto> selectCommentPageByAdminUno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectCommentPageByAdminUno", sc);
    }

}
