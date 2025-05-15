package com.nki.t1.dao;

import com.nki.t1.dto.FileDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileAnnounceDaoImpl implements FileAnnounceDao {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.FileAnnounceMapper.";

    @Override
    public int insertFile(FileDto fileDto) {
        return sqlSession.insert(namespace + "insertFile", fileDto);
    }

    @Override
    public FileDto selectFileByFnoUser(int fano) {
        return sqlSession.selectOne(namespace + "selectFileByFnoUser", fano);
    }

    @Override
    public FileDto selectFileByFnoAdmin(int fano) {
        return sqlSession.selectOne(namespace + "selectFileByFnoAdmin", fano);
    }

    @Override
    public List<FileDto> selectFileListByFnoListAdmin(List<Integer> fnoList) {
        return sqlSession.selectList(namespace + "selectFileListByFnoListAdmin", fnoList);
    }

    @Override
    public List<FileDto> selectFileListByAnoAdmin(int fano) {
        return sqlSession.selectList(namespace + "selectFileListByAnoAdmin", fano);
    }

    @Override
    public int deleteFileListByAnoAdmin(int ano) {
        return sqlSession.update(namespace + "deleteFileListByAnoAdmin", ano);
    }

    @Override
    public int deleteFileListByFanoAdmin(int fano) {
        return sqlSession.update(namespace + "deleteFileListByFanoAdmin", fano);
    }

    @Override
    public int deleteFileListByFnoListAdmin(List<Integer> fnoList) {
        return sqlSession.update(namespace + "deleteFileListByFnoListAdmin", fnoList);
    }
}
