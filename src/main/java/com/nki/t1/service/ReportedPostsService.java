package com.nki.t1.service;

import com.nki.t1.dto.ReportedPostsDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;

public interface ReportedPostsService {
    List<ReportedPostsDto> getReportsPage(SearchCondition sc);
    int countReports(SearchCondition sc);
    List<ReportedPostsDto> getReportsGroupByBrno(SearchCondition sc);
    int reportByUnoPno(ReportedPostsDto rp);
    ReportedPostsDto getReportByUnoPno(ReportedPostsDto rp);
    void deleteReportByUnoPno(ReportedPostsDto rp);

}
