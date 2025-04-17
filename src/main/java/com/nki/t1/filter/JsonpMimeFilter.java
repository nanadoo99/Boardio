package com.nki.t1.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * .jsonp 확장자를 가진 WebSocket 요청에 대해 MIME 타입을 application/javascript로 지정하는 필터입니다.
 * 특정 프록시나 브라우저 환경에서 WebSocket handshake fallback 시 Content-Type 오류를 방지하기 위해 사용됩니다.
 */
@WebFilter("/*")
public class JsonpMimeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        if (uri.contains("/websocket/") && uri.endsWith(".jsonp")) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setContentType("application/javascript");
        }
        chain.doFilter(request, response);
    }
}
