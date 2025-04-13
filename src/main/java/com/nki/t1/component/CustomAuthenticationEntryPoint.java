package com.nki.t1.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
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
