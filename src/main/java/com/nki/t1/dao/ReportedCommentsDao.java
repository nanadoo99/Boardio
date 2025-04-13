package com.nki.t1.dao;

import com.nki.t1.dto.ReportedCommentsDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;

public interface ReportedCommentsDao {
    List<ReportedCommentsDto> getReportsPage(SearchCondition sc);
    int countReports(SearchCondition sc);
    List<ReportedCommentsDto> getReportsGroupByBrno(SearchCondition sc);

    List<ReportedCommentsDto> getReportsByUnoCno(ReportedCommentsDto rp);
    int reportByUnoCno(ReportedCommentsDto rp);
    int updateReportByUnoCno(ReportedCommentsDto rp);
    int deleteReportByUnoCno(ReportedCommentsDto rp);
}