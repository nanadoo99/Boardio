package com.nki.t1.service;

import com.nki.t1.dao.ReportedCommentsDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ReportedCommentsDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.exception.InvalidCommentException;
import com.nki.t1.exception.InvalidReportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReportedCommentsServiceImpl implements ReportedCommentsService {

    private ReportedCommentsDao reportedCommentsDao;

    public ReportedCommentsServiceImpl(ReportedCommentsDao reportedCommentsDao) {
        this.reportedCommentsDao = reportedCommentsDao;
    }

    @Override
    public List<ReportedCommentsDto> getReportsPage(SearchCondition sc) {
        List<ReportedCommentsDto> rcList = reportedCommentsDao.getReportsPage(sc);
        if (rcList == null || rcList.size() == 0) {
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        }
        return rcList;
    }

    @Override
    public int countReports(SearchCondition sc) {
        try {
            return reportedCommentsDao.countReports(sc);
        } catch (DataAccessException e) {
            log.error("Database error occurred while getting report list - getReportsGroupByBrno()", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while getting report list - getReportsGroupByBrno()", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        }
    }

    @Override
    public List<ReportedCommentsDto> getReportsGroupByBrno(SearchCondition sc) {
        try {
            return reportedCommentsDao.getReportsGroupByBrno(sc);
        } catch (DataAccessException e) {
            log.error("Database error occurred while getting report list - getReportsGroupByBrno()", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while getting report list - getReportsGroupByBrno()", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        }
    }

    @Override
    public void reportByUnoCno(ReportedCommentsDto rc) {
        try {
            List<ReportedCommentsDto> rcList = reportedCommentsDao.getReportsByUnoCno(rc);
            int result;

            if(rcList == null || rcList.isEmpty()) {
                result = reportedCommentsDao.reportByUnoCno(rc);
            } else {
                result = reportedCommentsDao.updateReportByUnoCno(rc);
            }

            if(result != 1)
                throw new InvalidReportException(ErrorType.REPORT_NOT_CREATED);

        } catch (DataAccessException e) {
            log.error("Database error occurred while creating report", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_CREATED);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while creating report", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_CREATED);
        }
    }

    @Override
    public List<ReportedCommentsDto> getReportsByUnoCno(ReportedCommentsDto rc) {
        try {
            return reportedCommentsDao.getReportsByUnoCno(rc);
        } catch (DataAccessException e) {
            log.error("Database error occurred while getting report list", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while getting report list", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_FOUND);
        }
    }

    @Override
    public void deleteReportByUnoCno(ReportedCommentsDto rc) {
        try {
            int result = reportedCommentsDao.deleteReportByUnoCno(rc);
            if (result != 1) {
                throw new InvalidReportException(ErrorType.REPORT_NOT_DELETED);
            }
        } catch (DataAccessException e) {
            log.error("Database error occurred while deleting report", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_DELETED);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while deleting report", e);
            throw new InvalidReportException(ErrorType.REPORT_NOT_DELETED);
        }
    }

}
