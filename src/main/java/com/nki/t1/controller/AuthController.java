package com.nki.t1.controller;

import com.nki.t1.dto.UserDto;
import com.nki.t1.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/public")
@Controller
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping("/signup")
    public String showSignupForm(HttpServletRequest request, Model model) {
        setRedirectURL(request);
        return "signup";
    }
    //    @GetMapping("/login")
//    public String showLogin(HttpServletRequest request) {
//        System.out.println("@@@@@ /public/login");
////        String originURL = request.getHeader("Referer");
//        String redirctURL = request.getRequestURI();
//        if(!(redirctURL==null || redirctURL.contains("/login") || redirctURL.contains("/signup"))) {
//            request.getSession().setAttribute("redirctURL", redirctURL);
//        }
//        System.out.println("redirctURL = " + redirctURL);
//        return "login";
//    }
    private void setRedirectURL(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String referer = request.getHeader("Referer");

        System.out.println("@@@@@ setRedirectURL");
        System.out.println("requestURI: " + requestURI);
        System.out.println("referer = " + referer);

        HttpSession session = request.getSession();
        if (requestURI.contains("/login") || requestURI.contains("/signup")) {
            if (referer != null && !referer.contains("/login") && !referer.contains("/signup")) {
                session.setAttribute("redirectURL", referer);
            } else {
                session.setAttribute("redirectURL", "/"); // 기본 리다이렉트 URL
            }
        } else {
            session.setAttribute("redirectURL", requestURI);
        }
    }

    private String getRedirectURL(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String redirectURL = (String) session.getAttribute("redirectURL");
            if (redirectURL != null) {
                session.removeAttribute("redirectURL");
                return redirectURL;
            }
        }
        return "/";
    }


    @PostMapping("/idChk")
    public ResponseEntity<String> idChk(String id) { // Spring MVC에서 return 값을 Http 응답 객체로 설정하는 법.
        if(authService.idChk(id) != 0) {
            return ResponseEntity.ok("Unvail");
        }
        return ResponseEntity.ok("Avail");
    }

    @PostMapping("/signup")
    public String processSignup(UserDto user, HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("@@@@ /public/signup");

        try {
            int result = authService.register(user, request);

            if (result == 0) {
                throw new RuntimeException("Signup failed");
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", "SU_FAILED");
            model.addAttribute("user", user);
            return "signup";
        }

        String redirectURL = getRedirectURL(request);
        return "redirect:" + redirectURL;
    }


    @GetMapping("/login")
    public String showLogin(HttpServletRequest request) {
        setRedirectURL(request);
        return "login";
    }

//    @GetMapping("/login")
//    public String showLogin(HttpServletRequest request) {
//        System.out.println("@@@@@ /public/login");
////        String originURL = request.getHeader("Referer");
//        String redirctURL = request.getRequestURI();
//        if(!(redirctURL==null || redirctURL.contains("/login") || redirctURL.contains("/signup"))) {
//            request.getSession().setAttribute("redirctURL", redirctURL);
//        }
//        System.out.println("redirctURL = " + redirctURL);
//        return "login";
//    }

    @GetMapping("/fail")
    public String loginFail(HttpServletRequest request) {
        // 세션 종료
        request.getSession(false).invalidate();
        return "loginFail";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 종료
//        request.getSession(false).invalidate();
        return "logout";
    }
}
