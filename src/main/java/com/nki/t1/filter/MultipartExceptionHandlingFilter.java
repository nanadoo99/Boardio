package com.nki.t1.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 파일 업로드 용량 초과(MaxUploadSizeExceededException) 예외를 처리하기 위한 필터입니다.
 * DispatcherServlet 이전에 발생하는 예외이기 때문에,
 * 이 필터에서 직접 JSON 형식으로 응답을 내려줍니다.
 * 응답이 이미 커밋된 경우엔 예외를 그대로 전파합니다.
 */
@Slf4j
public class MultipartExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(req, res);
        } catch (MaxUploadSizeExceededException e) {
            HttpServletResponse response = (HttpServletResponse) res;

            // ⚠️ 업로드 용량 초과 예외 발생 시 JSON 형태로 응답 처리
            if (!response.isCommitted()) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                // 사용자에게 제한 초과 안내 메시지 전송
                response.getWriter().write("{\"errorResponse\": {\"message\": \"업로드 용량이 초과되었습니다. 최대 2MB까지 업로드 가능합니다.\"}}");
                log.info("response.toString(): " + response.toString());
                return;
            }

            // 이미 응답이 커밋된 경우는 예외 전파 (컨트롤러에서 처리 불가)
            throw e;
        }
    }
}
