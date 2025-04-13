package com.nki.t1.service;

import com.nki.t1.dto.ReportedCommentsDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;

public interface ReportedCommentsService {
    List<ReportedCommentsDto> getReportsPage(SearchCondition sc);
    int countReports(SearchCondition sc);
    List<ReportedCommentsDto> getReportsGroupByBrno(SearchCondition sc);
    void reportByUnoCno(ReportedCommentsDto rp);
    List<ReportedCommentsDto> getReportsByUnoCno(ReportedCommentsDto rp);
    void deleteReportByUnoCno(ReportedCommentsDto rp);

}
