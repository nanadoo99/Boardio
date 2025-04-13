package com.nki.t1.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 필요한 권한이 존재하지 않을 때, 403 Forbidden 에러 리턴.
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 응답 상태 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        // 최상위 객체에 errorResponse 키 추가
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("errorResponse", new ErrorResponse(ErrorType.REQUEST_UNAUTH));

        // JSON 변환 및 응답
        String jsonResponse = objectMapper.writeValueAsString(responseMap);
        response.getWriter().write(jsonResponse);
    }
}
