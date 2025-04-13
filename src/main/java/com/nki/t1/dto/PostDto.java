package com.nki.t1.dto;

import com.nki.t1.domain.CkEditorPost;
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
public class PostDto extends BlockReasonsDto implements UserIdentifiable, CkEditorPost {
    private Integer pno;
    private Integer uno;
    private String userId;
    @NotBlank
    @Size(min = 1, max = 100, message = "100 이하로 입력하세요.")
    private String title;
    @NotBlank
    private String content;
    private String contentTxt;
    private Integer views;

    private Integer cmtCnt;
    private Integer rpnoCnt;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Date reportedAt;
    private Date blockedAt;

    private boolean del;
    private ContentState contentState;

    public PostDto() {
        this(0, "", "");
    }

    public PostDto(Integer uno, String title, String content) {
        this.uno = uno;
        this.title = title;
        this.content = content;
    }

    public String getContentStateKorNm() {
        return contentState != null? contentState.getKorNm() : "";
    }

    @Override
    public Integer getPostId() {
        return this.pno;
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
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

    public String getFormattedReportedAtTime() {
        return getFormattedDateTime(this.reportedAt);
    }

    public String getFormattedBlockedAtTime() {
        return getFormattedDateTime(this.blockedAt);
    }

}
