/*
package com.nki.t1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("@@@@@ LoginInterceptor_preHandle");
        System.out.println("requestUri" + requestUri);
*/
/*        // 로그인된 사용자가 로그인 페이지에 접근하려고 할 때
        if (requestUri.contains("/login") && auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            System.out.println("@@@@@ Chk");
            response.sendRedirect("/");
        }*//*

        return true;
    }
}
*/
