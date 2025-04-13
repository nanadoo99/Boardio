package com.nki.t1.dao;

import com.nki.t1.dto.BlockReasonsDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlockReasonDaoImpl implements BlockReasonDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.BlockReasonsMapper.";

    @Override
    public List<BlockReasonsDto> selectBlockReasons() {
        return sqlSession.selectList(namespace + "selectBlockReasons");
    }

}
