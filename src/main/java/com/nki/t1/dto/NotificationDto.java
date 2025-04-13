package com.nki.t1.dto;

import com.nki.t1.domain.NotificationType;
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
public class NotificationDto{
    private Integer id;
    private Integer uno;
    private NotificationType type;
    private Integer targetParentIdx; // 게시글 번호 등
    private String targetParentText; // 게시글 제목 등
    private Integer targetChildIdx; // 게시글에 속한 댓글 번호 등
    private String targetChildText; // 게시글에 속한 댓글 제목 등
    private boolean isRead;
    private Date createdAt;
    private boolean del;

    public NotificationDto(){}

    public boolean getIsRead(){
        return isRead;
    }

    public String getTypeKorName() {
        return this.type.getKorName();
    }

    public String getTargetUri() {
        switch (this.type) {
            case NEW_COMMENT_TO_POST:
                return "/user/mypost/read/" + this.targetParentIdx;
            case NEW_ANNOUNCE:
                return "/user/announces/" + this.targetParentIdx;
            default:
                return "/user/announces";
        }
    }

    public static class Builder{
        private Integer uno;
        private NotificationType type;
        private Integer targetParentIdx; // 게시글 번호 등
        private Integer targetChildIdx; // 게시글에 속한 댓글 번호 등

        public Builder uno(Integer uno){
            this.uno = uno;
            return this;
        }
        public Builder type(NotificationType type){
            this.type = type;
            return this;
        }
        public Builder targetParentIdx(Integer targetParentIdx){
            this.targetParentIdx = targetParentIdx;
            return this;
        }
        public Builder targetChildIdx(Integer targetChildIdx){
            this.targetChildIdx = targetChildIdx;
            return this;
        }

        public NotificationDto build(){
            return new NotificationDto(this);
        }
    }

    private NotificationDto(Builder builder){
        this.uno = builder.uno;
        this.type = builder.type;
        this.targetParentIdx = builder.targetParentIdx;
        this.targetChildIdx = builder.targetChildIdx;
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
    }

    // Date 포맷팅 - 날짜 및 시간
    private String getFormattedDateTime(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedCreatedAtTime() {
        return getFormattedDateTime(this.createdAt);
    }
}
