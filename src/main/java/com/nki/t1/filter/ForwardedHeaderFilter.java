package com.nki.t1.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Slf4j
public class ForwardedHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String forwardedProto = httpRequest.getHeader("X-Forwarded-Proto");

        log.info("X-Forwarded-Proto: {}", forwardedProto); // 🚀 헤더 값 확인

        if ("https".equalsIgnoreCase(forwardedProto)) {
            request = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public boolean isSecure() {
                    return true;
                }

                @Override
                public String getScheme() {
                    return "https";
                }
            };
        }

        chain.doFilter(request, response);
    }
}