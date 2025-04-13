package com.nki.t1.dto;

import com.nki.t1.domain.UserIdentifiable;
import com.nki.t1.domain.ContentState;
import com.nki.t1.utils.DateTimeFormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CommentDto extends BlockReasonsDto implements UserIdentifiable {
    private Integer cno;
    private Integer pno;
    private String postTitle;
    private Integer pcno;
    private Integer uno;
    private String userId;
    private String parentUserId;
    @NotBlank
    @Size(min = 1, max = 100, message = "댓글은 1000 이하로 입력이 가능합니다.")
    private String comment;
    private Date createdAt;
    private Date updatedAt;
    private Date reportedAt;
    private Date blockedAt;
    private Date deletedAt;
    private boolean del;

    private ContentState contentState;
    private ReportedCommentsDto reportedCommentsDto;
    private Integer rcnoCnt;

    public CommentDto() {
    }

    public CommentDto(Integer pno, Integer uno, String comment) {
        this.pno = pno;
        this.uno = uno;
        this.comment = comment;
    }

    public String getContentStateKorNm() {
        return contentState != null ? contentState.getKorNm() : "";
    }

    public Integer getRcno() {
        return reportedCommentsDto != null ? reportedCommentsDto.getRcno() : null;
    }

    public Integer getReportUno() {
        return reportedCommentsDto != null ? reportedCommentsDto.getReportUno() : null;
    }

    public Integer getBrno() {
        return reportedCommentsDto != null ? reportedCommentsDto.getBrno() : null;
    }

    public String getBrText() {
        return reportedCommentsDto != null ? reportedCommentsDto.getBrText() : null;
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
    }

    public String getFormattedUpdatedAt() {
        return getFormattedDate(this.updatedAt);
    }

    public String getFormattedReportedAt() {
        return getFormattedDate(this.reportedAt);
    }

    public String getFormattedBlockedAt() {
        return getFormattedDate(this.blockedAt);
    }

    // Date 포맷팅 - 날짜 및 시간
    private String getFormattedDateTime(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedCreatedAtTime() {
        return getFormattedDateTime(this.createdAt);
    }

    public String getFormattedUpdatedAtTime() {
        return getFormattedDateTime(this.updatedAt);
    }

    public String getFormattedReportedAtTime() {
        return getFormattedDateTime(this.reportedAt);
    }

    public String getFormattedBlockedAtTime() {
        return getFormattedDateTime(this.blockedAt);
    }
}
