package com.nki.t1.dao;

import com.nki.t1.dto.FileDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FileCkEditorDaoPostImpl implements FileCkEditorDao {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.FilePostCkMapper.";

    @Override
    public int insertFile(FileDto fileDto) {
        return sqlSession.insert(namespace + "insertFile", fileDto);
    }

    @Override
    public Set<FileDto> selectFileDto(int idx) {
        List<FileDto> list = sqlSession.selectList(namespace + "selectFileDto", idx);
        return new HashSet<>(list);
    }

    @Override
    public Set<String> selectAllUids(long expirationPeriod) {
        List<String> list = sqlSession.selectList(namespace + "selectAllUids", expirationPeriod);
        return new HashSet<>(list);
    }

    @Override
    public Set<String> selectUid(int idx) {
        List<String> list = sqlSession.selectList(namespace + "selectUid", idx);
        return new HashSet<>(list);
    }

    @Override
    public int deleteFileByPostIdx(int idx) {
        return sqlSession.delete(namespace + "deleteFileByPno", idx);
    }

    @Override
    public int deletePartiallyByFno(int idx) {
        return sqlSession.delete(namespace + "deletePartiallyByFno", idx);
    }

    @Override
    public void deleteFileByFileUuid(String fileUuid) {
        sqlSession.delete(namespace + "deleteFileByFileUuid", fileUuid);
    }

}
