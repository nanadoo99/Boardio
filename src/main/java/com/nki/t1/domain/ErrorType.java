package com.nki.t1.domain;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_AUTHENTICATED(1001, "인증되지 ddd 않았습니다."),

    USER_REG_EXIST(2001, "이미 가입된 이메일입니다."),
    USER_UNMATCHED(2002, "이메일과 비밀번호를 확인하세요."),
    USER_NAME_NOT_FOUND(2002, "이메일을 확인하세요."),
    USER_MISSING(2003, "이메일과 비밀번호를 입력하세요."),
    USER_LOCKED(2004, "계정이 비활성화 되었습니다. 관리자에게 문의하세요."),
    USER_MAIL_UNAUTH(2005, "이메일이 인증되지 않았습니다."),

    POST_NOT_FOUND(3001, "게시글을 불러올 수 없습니다."),
    POST_NOT_CREATED(3002, "게시글을 등록할 수 없습니다."),
    POST_DELETED(3003, "삭제된 게시글입니다."),
    POST_BLOCKED(3004, "차단된 게시글입니다."),
    POST_UNAUTH(3005, "게시글에 대한 요청하신 권한이 없습니다."),
    POST_NOT_UPDATED(3006, "게시글을 수정할 수 없습니다."),

    COMMENT_NOT_FOUND(4001, "댓글을 불러올 수 없습니다."),
    COMMENT_NOT_CREATED(4002, "댓글을 등록할 수 없습니다."),
    COMMENT_NOT_UPDATED(4003, "댓글을 수정할 수 없습니다."),
    COMMENT_NOT_DELETED(4004, "댓글을 삭제할 수 없습니다."),

    ANNOUNCE_NOT_FOUND(5001, "공지사항을 불러올 수 없습니다."),
    ANNOUNCE_NOT_CREATED(5002, "공지사항을 등록할 수 없습니다."),
    ANNOUNCE_NOT_UPDATED(5003, "공지사항을 수정할 수 없습니다."),
    ANNOUNCE_NOT_DELETED(5004, "공지사항을 삭제할 수 없습니다."),
    ANNOUNCE_INVALID_POSTED_AT(5005, "게시일을 확인하세요."),

    FILE_NOT_FOUND(6001, "파일을 불러올 수 없습니다."),
    FILE_IS_EMPTY(6002, "업로드된 파일이 비어있습니다."),
    FILE_SIZE_EXCEEDED(6003, "파일이 업로드 최대크기를 초과합니다."),
    FILE_TOTAL_SIZE_EXCEEDED(6003, "전체 업로드 요청이 최대크기를 초과합니다."),
    FILE_NOT_DELETED(6004, "파일을 삭제할 수 없습니다."),
    FILE_PATH_CREATION_FAILED(6005, "파일 저장 경로를 생성할 수 없습니다."),

    REPORT_NOT_FOUND(7001, "신고 내역을 불러올 수 없습니다."),
    REPORT_NOT_CREATED(7002, "신고를 진행할 수 없습니다."),
    REPORT_NOT_DELETED(7003, "신고를 취소할 수 없습니다."),

    EMAIL_(1001, ""),

    REQUEST_UNAUTH(9001, "요청하신 권한ddd이 없습니다. 관리자에게 문의하세요."),
    REQUEST_FAILED(9999, "요청을 완료하지 못했습니다. 관리자에게 문의하세요.");

    final int code;
    private final String message;

    ErrorType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
