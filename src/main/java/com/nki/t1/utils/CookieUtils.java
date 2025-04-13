/*
package com.nki.t1.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {

    public void setRefererURL(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        String refererURL = "/";

        System.out.println("@@@@@ setRefererURL");
        System.out.println("referer = " + referer); // 요청을 보낸 페이지

        // 로그인 혹은 회원가입
        if (referer != null && !referer.contains("/login") && !referer.contains("/signup")) {
            refererURL = referer;
        }

        Cookie cookie = new Cookie("refererURL", refererURL);
        cookie.setPath("/"); // 쿠키의 유효 범위 설정
        cookie.setHttpOnly(true); // 클라이언트 측에서 JavaScript로 접근 불가
        cookie.setMaxAge(60 * 5); // 쿠키 유효 기간 설정 (5분)
        response.addCookie(cookie);
        System.out.println("----- cookie : " + cookie);
    }

    public String getRefererURL(HttpServletRequest request, HttpServletResponse response) {
        String refererURL = "/";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if("refererURL".equals(cookie.getName())) {
                    refererURL = cookie.getValue();
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        System.out.println("----- getRefererURL : " + refererURL);
        return refererURL;
    }

    public void setRedirectURI(HttpServletResponse response, String uri) {

        Cookie cookie = new Cookie("redirectURI", uri);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 5);
        response.addCookie(cookie);
        System.out.println("----- cookie : " + cookie);
    }

    public String getRedirectURI(HttpServletRequest request, HttpServletResponse response) {
        String redirectURI = "/";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if("redirectURI".equals(cookie.getName())) {
                    redirectURI = cookie.getValue();
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        System.out.println("----- getRefererURL : " + redirectURI);
        return redirectURI;
    }
}*/
