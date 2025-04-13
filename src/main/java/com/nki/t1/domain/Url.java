package com.nki.t1.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Url {
    //  =====  일반 사용자  =====  //
    USER_HOME("홈", "/", null),

    USER_POST("게시판", "/user/post?page=1", USER_HOME),
    USER_ANNOUNCE("공지사항", "/user/announces?page=1", USER_HOME),
    USER_MYPAGE("마이페이지", null, USER_HOME),

    USER_MYPAGE_POST("작성 글", "/user/mypost/list", USER_MYPAGE),
    USER_MYPAGE_COMMENT("작성 댓글", "/user/mycomments/list", USER_MYPAGE),
    USER_MYPAGE_PROFILE("회원정보", "/user/profile", USER_MYPAGE),

    //  =====  관리자  =====  //
    ADMIN_HOME("관리자 홈", "/admin", null),

    ADMIN_USERINFO("회원 관리", "/admin/user-management?page=1", ADMIN_HOME),
    ADMIN_ANNOUNCE("공지사항 관리", null, ADMIN_HOME),
    ADMIN_BANNER("배너 관리", null, ADMIN_HOME),
    ADMIN_POST("게시글 관리", "/admin/posts?page=1", ADMIN_HOME),
    ADMIN_COMMENT("댓글 관리", "/admin/comments?page=1", ADMIN_HOME),

    ADMIN_BANNER_SCHEDULE("게시 일정 및 등록", "/admin/banners", ADMIN_BANNER),
    ADMIN_BANNER_DEFAULT("기본 배너", "/admin/banners/default", ADMIN_BANNER),

    ADMIN_ANNOUNCE_SCHEDULE("게시 일정", "/admin/announces/schedule", ADMIN_ANNOUNCE),
    ADMIN_ANNOUNCE_LIST("전체 목록", "/admin/announces?page=1", ADMIN_ANNOUNCE);

    private final String nameKor;
    private final String url;
    private final Url parent;

    Url(String nameKor, String url, Url parent) {
        this.nameKor = nameKor;
        this.url = url;
        this.parent = parent;
    }

    public static List<Url> getPath(Url current) {
        List<Url> path = new ArrayList<Url>();
        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }
        return path;
    }
}
