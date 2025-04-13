package com.nki.t1.dao;

import com.nki.t1.dto.ReportedPostsDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;

public interface ReportedPostsDao {
    List<ReportedPostsDto> getReportsPage(SearchCondition sc);
    int countReports(SearchCondition sc);
    List<ReportedPostsDto> getReportsGroupByBrno(SearchCondition sc);

    ReportedPostsDto getReportByUnoPno(ReportedPostsDto rp);
    int reportByUnoPno(ReportedPostsDto rp);
    int updateReportByUnoPno(ReportedPostsDto rp);
    int deleteReportByUnoPno(ReportedPostsDto rp);
}