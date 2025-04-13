package com.nki.t1.dto;

import com.nki.t1.domain.CkEditorPost;
import com.nki.t1.domain.UserIdentifiable;
import com.nki.t1.utils.DateTimeFormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AnnounceDto implements UserIdentifiable, CkEditorPost, ObjDto {
    private Integer ano;
    private Integer uno;
    private String userId;
    @NotBlank
    @Size(min = 1, max = 100, message = "{size}")
    private String title;
    @NotBlank
    private String content;
    private String contentTxt;
    private Date createdAt; // 작성일
    private Date postedAt;  // 공개 게시일
    private Date updatedAt;
    private Date deletedAt;
    private int fileCnt;
    private List<FileDto> fileDtoList;
    private List<Integer> fnoList;
    private List<MultipartFile> attachedFiles;

    @Override
    public Integer getPostId() {
        return this.ano;
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
    }

    public String getFormattedPostedAt() {
        return getFormattedDate(this.postedAt);
    }

    public String getFormattedUpdatedAt() {
        return getFormattedDate(this.updatedAt);
    }

    // Date 포맷팅 - 날짜 및 시간
    private String getFormattedDateTime(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedCreatedAtTime() {
        return getFormattedDateTime(this.createdAt);
    }

    public String getFormattedPostedAtTime() {
        return getFormattedDateTime(this.postedAt);
    }

    public String getFormattedUpdatedAtTime() {
        return getFormattedDateTime(this.updatedAt);
    }

}
