package com.nki.t1.dao;

import com.nki.t1.dto.VisitorDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VisitorsDao {
    @Autowired
    private SqlSession sqlSession;
    private static String namespace = "com.nki.t1.dao.VisitorsMapper.";

    public boolean existsByUserIpAndDate(VisitorDto visitorDto) {
        return sqlSession.selectOne(namespace + "existsByUserIpAndDate", visitorDto);
    }

    public void insertVisitor(VisitorDto visitorDto) {
        sqlSession.insert(namespace + "insertVisitor", visitorDto);
    }

    public VisitorDto countVisitorsToday() {
        return sqlSession.selectOne(namespace + "countVisitorsToday");
    }

    public VisitorDto countVisitorsTotal() {
        return sqlSession.selectOne(namespace + "countVisitorsTotal");
    }
}
