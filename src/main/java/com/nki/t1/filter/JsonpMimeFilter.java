package com.nki.t1.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
