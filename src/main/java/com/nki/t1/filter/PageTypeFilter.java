package com.nki.t1.filter;

import com.nki.t1.domain.PageNavType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * PageTypeFilter는 요청 URI를 분석하여 현재 페이지 유형(PageNavType)을 추출하고,
 * 이를 request attribute로 설정해주는 필터입니다.
 *
 * Spring Security 필터 체인에 등록되어 있어,
 * 컨트롤러 진입 전 필터 단계에서 요청별 페이지 구분 정보를 세팅합니다.
 *
 * 이 값은 뷰 렌더링 단계에서 메뉴 탭 활성화, 내비게이션 상태 유지 등에 활용됩니다.
 * 필터 순서는 인증 필터 이전(UsernamePasswordAuthenticationFilter보다 앞)으로 설정되어 있어,
 * 인증 여부와 무관하게 모든 요청에 대해 페이지 타입 분석이 가능합니다.
 */

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