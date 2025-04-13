package com.nki.t1.dao;

import com.nki.t1.dto.ReportedCommentsDto;
import com.nki.t1.dto.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportedCommentsDaoImpl implements ReportedCommentsDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.ReportedCommentsMapper.";

    @Override
    public List<ReportedCommentsDto> getReportsPage(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getReportsPage", sc);
    }

    @Override
    public int countReports(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countReports", sc);
    }

    @Override
    public List<ReportedCommentsDto> getReportsGroupByBrno(SearchCondition sc) {
        return sqlSession.selectList(namespace + "getReportsGroupByBrno", sc);
    }

    @Override
    public List<ReportedCommentsDto> getReportsByUnoCno(ReportedCommentsDto rp) {
        return sqlSession.selectList(namespace + "getReportsByUnoCno", rp);
    }

    @Override
    public int reportByUnoCno(ReportedCommentsDto rp) {
        return sqlSession.insert(namespace + "reportByUnoCno", rp);
    }

    @Override
    public int updateReportByUnoCno(ReportedCommentsDto rp) {
        return sqlSession.update(namespace + "updateReportByUnoCno", rp);
    }

    @Override
    public int deleteReportByUnoCno(ReportedCommentsDto rp) {
        return sqlSession.update(namespace + "deleteReportByUnoCno", rp);
    }
}
