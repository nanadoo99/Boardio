package com.nki.t1.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
    NEW_COMMENT_TO_POST("게시물 새 댓글"),
    NEW_ANNOUNCE("새 공지");
    private final String korName;

    NotificationType(String korName) {
        this.korName = korName;
    }
    public String getKorName() {
        return korName;
    }
}
