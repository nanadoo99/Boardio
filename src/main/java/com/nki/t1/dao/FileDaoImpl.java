package com.nki.t1.dao;

import com.nki.t1.dto.PostDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class FileDaoImpl implements FileDao {
    @Autowired
    private SqlSession sqlSession;
    private String namespace = "com.nki.t1.dao.FilesMapper.";

    @Override
    public int insertFile(Map<String, Object> map) {
        return sqlSession.insert(namespace + "insertFile", map);
    }

}
