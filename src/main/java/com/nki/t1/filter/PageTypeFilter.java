package com.nki.t1.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.nki.t1.domain.PageNavType;
import org.springframework.stereotype.Component;

@Component
public class PageTypeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String baseUri = getBaseUri(uri);
        String pageNavTypeStr = null;

        if (uri != null && !uri.isEmpty()) {
            for (PageNavType pageNavType : PageNavType.values()) {
                if (baseUri.toLowerCase().contains(pageNavType.name().toLowerCase())) {
                    pageNavTypeStr = pageNavType.name().toLowerCase();
                    break;
                }
            }
        }

        servletRequest.setAttribute("pageNavType", pageNavTypeStr);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getBaseUri(String uri) {
        int firstSlash = uri.indexOf('/');
        int secondSlash = firstSlash == -1 ? -1 : uri.indexOf('/', firstSlash + 1);
        int thirdSlash = secondSlash == -1 ? -1 : uri.indexOf('/', secondSlash + 1);
        int fourthSlash = thirdSlash == -1 ? -1 : uri.indexOf('/', thirdSlash + 1);

        if (fourthSlash == -1) {
            return uri;
        } else {
            return uri.substring(0, fourthSlash);
        }
    }

    @Override
    public void destroy() {
    }
}