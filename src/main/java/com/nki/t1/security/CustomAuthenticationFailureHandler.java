package com.nki.t1.security;

import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${auth.max.attempts}")
    private Integer maxAttempts;

    @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ErrorType errorType;
        String addedMessage = "";

        exception.printStackTrace();
        if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorType = ErrorType.USER_MISSING;
        } else if (exception instanceof UsernameNotFoundException) {
            errorType = ErrorType.USER_NAME_NOT_FOUND;
        } else if (exception instanceof BadCredentialsException) {
            errorType = ErrorType.USER_UNMATCHED;
            String count = exception.getMessage();
            addedMessage = " " + maxAttempts + "회 이상 오류시 계정이 잠깁니다. (" + count + "/" + maxAttempts + ")";
        } else if (exception instanceof LockedException) {
            errorType = ErrorType.USER_LOCKED;
        } else {
            errorType = ErrorType.REQUEST_FAILED;
        }

        UserDto userDto = UserDto.builder()
                .id(request.getParameter("id"))
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .build();

        ErrorResponse errorResponse = new ErrorResponse(errorType.getCode(), errorType.getMessage()+addedMessage);
        request.setAttribute("errorResponse", errorResponse);
        request.setAttribute("userDto", userDto);

        request.getRequestDispatcher("/auth/login").forward(request, response);
    }
}
