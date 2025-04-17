package com.nki.t1.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MultipartExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(req, res);
        } catch (MaxUploadSizeExceededException e) {
            HttpServletResponse response = (HttpServletResponse) res;

            // 응답이 아직 커밋되지 않았다면 JSON 형태로 직접 써버림
            if (!response.isCommitted()) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"errorResponse\": {\"message\": \"업로드 용량이 초과되었습니다. 최대 2MB까지 업로드 가능합니다.\"}}");
                log.info("response.toString(): " + response.toString());
                return;
            }

            // 이미 커밋됐으면 못 막음
            throw e;
        }
    }
}
