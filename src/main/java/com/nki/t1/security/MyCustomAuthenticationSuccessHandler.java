package com.nki.t1.security;

import com.nki.t1.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class MyCustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public MyCustomAuthenticationSuccessHandler() {
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("@@@@@ MyCustomAuthenticationSuccessHandler_onAuthenticationSuccess");
        HttpSession session = request.getSession();
        if(session != null) {
            String redirctURL = (String) session.getAttribute("redirctURL");
            if(redirctURL != null) {
                session.removeAttribute("redirctURL");
                getRedirectStrategy().sendRedirect(request, response, redirctURL);
            } else{
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
        setUserInSession(request, authentication);
    }

    public void setUserInSession(HttpServletRequest request, Authentication authentication) {
        System.out.println("@@@@@ MyCustomAuthenticationSuccessHandler_setUserInSession");
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDto) {
            UserDto userDto = (UserDto) principal;
            System.out.println("----- userDto =" + userDto);
            String userId = userDto.getId();  // UserDetailsImpl 에서 getUsername() > return member.getId()
            HttpSession session = request.getSession();
            if(userId != null || !userId.isEmpty()) {
                session.setAttribute("authUser", userDto);
            }
        }
    }
}
