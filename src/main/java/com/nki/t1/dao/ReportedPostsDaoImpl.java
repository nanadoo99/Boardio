package com.nki.t1.dao;

import com.nki.t1.dto.ReportedPostsDto;
import com.nki.t1.dto.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportedPostsDaoImpl implements ReportedPostsDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.ReportedPostsMapper.";

    @Override
    public List<ReportedPostsDto> getReportsPage(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getReportsPage", sc);
    }

    @Override
    public int countReports(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countReports", sc);
    }

    @Override
    public List<ReportedPostsDto> getReportsGroupByBrno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getReportsGroupByBrno", sc);
    }

    @Override
    public ReportedPostsDto getReportByUnoPno(ReportedPostsDto rp) {
        return sqlSession.selectOne(namespace + "getReportByUnoPno", rp);
    }

    @Override
    public int reportByUnoPno(ReportedPostsDto rp) {
        return sqlSession.insert(namespace + "reportByUnoPno", rp);
    }

    @Override
    public int updateReportByUnoPno(ReportedPostsDto rp) {
        return sqlSession.update(namespace + "updateReportByUnoPno", rp);
    }

    @Override
    public int deleteReportByUnoPno(ReportedPostsDto rp) {
        return sqlSession.update(namespace + "deleteReportByUnoPno", rp);
    }
}
