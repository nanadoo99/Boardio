package com.nki.t1.dto;

import com.nki.t1.utils.DateTimeFormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReportedCommentsDto extends BlockReasonsDto {
    private Integer rcno;
    private Integer cno;
    private Integer reportUno;
    private String reportUserId;
    private Date reportedAt;

    private Integer deleteUno;
    private Date deletedAt;

    public ReportedCommentsDto() {}

    public ReportedCommentsDto(int cno, int reportUno) {
        this.cno = cno;
        this.reportUno = reportUno;
    }

    public ReportedCommentsDto(int cno, int reportUno, int deleteUno) {
        this.cno = cno;
        this.reportUno = reportUno;
        this.deleteUno = deleteUno;
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedReportedAt() {
        return getFormattedDate(this.reportedAt);
    }

    // Date 포맷팅 - 날짜 및 시간
    private String getFormattedDateTime(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedReportedAtTime() {
        return getFormattedDateTime(this.reportedAt);
    }
}
