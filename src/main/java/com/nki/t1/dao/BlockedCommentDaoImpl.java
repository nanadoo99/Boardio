package com.nki.t1.dao;

import com.nki.t1.dto.BlockedCommentDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlockedCommentDaoImpl implements BlockedCommentDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.BlockedCommentsMapper.";


    @Override
    public int blockComment(BlockedCommentDto blockedCommentDto) {
        return sqlSession.insert(namespace + "blockComment", blockedCommentDto);
    }

    @Override
    public int deleteBlockedComment(BlockedCommentDto blockedCommentDto) {
        return sqlSession.update(namespace + "deleteBlockedComment", blockedCommentDto);
    }

    @Override
    public List<BlockedCommentDto> selectBlocksByPno(int cno) {
        return sqlSession.selectList(namespace + "selectBlocksByPno", cno);
    }
}
