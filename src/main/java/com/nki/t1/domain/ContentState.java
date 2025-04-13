package com.nki.t1.domain;

import lombok.Getter;

@Getter
public enum ContentState {
    UNREPORTED_UNBLOCKED(0, "미접수"),
    REPORTED(1, "신고 접수"),
    BLOCKED(2, "관리자 차단");

    private final int code;
    private final String KorNm;

    private ContentState(int code, String KorNm) {
        this.code = code;
        this.KorNm = KorNm;
    }

    public String getKorNm() {
        return KorNm;
    }

    public static ContentState fromCode(int code) {
        for(ContentState contentState : ContentState.values()) {
            if(contentState.code == code) {
                return contentState;
            }
        }
        throw new IllegalArgumentException("Unknown code " + code);
    }
}
