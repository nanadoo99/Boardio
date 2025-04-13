package com.nki.t1.dao;

import com.nki.t1.dto.BlockedPostDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlockedPostDaoImpl implements BlockedPostDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.BlockedPostsMapper.";


    @Override
    public int blockPost(BlockedPostDto blockedPostDto) {
        return sqlSession.insert(namespace + "blockPost", blockedPostDto);
    }

    @Override
    public int deleteBlockedPost(BlockedPostDto blockedPostDto) {
        return sqlSession.update(namespace + "deleteBlockedPost", blockedPostDto);
    }

    @Override
    public List<BlockedPostDto> selectBlocksByPno(int pno) {
        return sqlSession.selectList(namespace + "selectBlocksByPno", pno);
    }
}
