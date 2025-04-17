package com.nki.t1.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 클라이언트가 HTTPS로 접속했는지 판단할 수 있게 도와주는 필터입니다.
 * 리버스 프록시(예: AWS ELB) 뒤에 있을 때, 서버가 요청을 HTTP로 착각하지 않도록
 * X-Forwarded-Proto 헤더를 보고 HTTPS 요청임을 강제로 인식시킵니다.
 */
@Slf4j
public class ForwardedHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String forwardedProto = httpRequest.getHeader("X-Forwarded-Proto");

        log.info("X-Forwarded-Proto: {}", forwardedProto);

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