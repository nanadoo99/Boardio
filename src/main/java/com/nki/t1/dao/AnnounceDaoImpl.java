package com.nki.t1.dao;

import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnounceDaoImpl implements AnnounceDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.AnnounceMapper.";


    @Override
    public int countAnnounceAdmin(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countAnnounceAdmin", sc);
    }

    @Override
    public List<AnnounceDto> getAnnouncePageAdmin(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getAnnouncePageAdmin", sc);
    }

    @Override
    public AnnounceDto selectAnnounceAdmin(int ano) {
        return sqlSession.selectOne(namespace + "selectAnnounceAdmin", ano);
    }


    @Override
    public int insertAnnounce(AnnounceDto announceDto) {
        return sqlSession.insert(namespace + "insertAnnounce", announceDto);
    }

    @Override
    public void updateAnnounce(AnnounceDto announceDto) {
        sqlSession.update(namespace + "updateAnnounce", announceDto);
    }

    @Override
    public int deleteAnnounce(AnnounceDto announceDto) {
        return sqlSession.update(namespace + "deleteAnnounce", announceDto);
    }

    @Override
    public int countAnnounceUser(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countAnnounceUser", sc);
    }

    @Override
    public List<AnnounceDto> getAnnouncePageUser(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getAnnouncePageUser", sc);
    }

    @Override
    public AnnounceDto selectAnnounceUser(int ano) {
        return sqlSession.selectOne(namespace + "selectAnnounceUser", ano);
    }

    @Override
    public List<AnnounceDto> selectAnnounceListByPostedAt(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectAnnounceListByPostedAt", sc);
    }

    @Override
    public List<AnnounceDto> selectAnnounceScheduleListByPostedAt() {
        return sqlSession.selectList(namespace + "selectAnnounceScheduleListByPostedAt");
    }
}
