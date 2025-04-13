package com.nki.t1.service;

import com.nki.t1.dao.ReportedPostsDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ReportedPostsDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.exception.InvalidReportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReportedPostsServiceImpl implements ReportedPostsService {
    private ReportedPostsDao reportedPostsDao;

    public ReportedPostsServiceImpl(ReportedPostsDao reportedPostsDao) {
        this.reportedPostsDao = reportedPostsDao;
    }

    @Override
    public List<ReportedPostsDto> getReportsPage(SearchCondition sc) {
        return reportedPostsDao.getReportsPage(sc);
    }

    @Override
    public int countReports(SearchCondition sc) {
        return reportedPostsDao.countReports(sc);
    }

    @Override
    public List<ReportedPostsDto> getReportsGroupByBrno(SearchCondition sc) {
        return reportedPostsDao.getReportsGroupByBrno(sc);
    }

    @Override
    public int reportByUnoPno(ReportedPostsDto rp) {
        ReportedPostsDto report = reportedPostsDao.getReportByUnoPno(rp);
        if (report == null) {
            return reportedPostsDao.reportByUnoPno(rp);
        } else {
            return reportedPostsDao.updateReportByUnoPno(rp);
        }
    }

    @Override
    public ReportedPostsDto getReportByUnoPno(ReportedPostsDto rp) {
        try {
            return reportedPostsDao.getReportByUnoPno(rp);
        } catch (DataAccessException e) {
            log.error("DataAccessException while getting reports by UnoPno", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        } catch (Exception e) {
            log.error("RuntimeException while getting reports by UnoPno", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        }
    }

    @Override
    public void deleteReportByUnoPno(ReportedPostsDto rp) {
        try {
            reportedPostsDao.deleteReportByUnoPno(rp);
        } catch (Exception e) {
            log.error("RuntimeException while deleting report", e);
            throw new InvalidReportException(ErrorType.REQUEST_FAILED);
        }
    }

}
