package com.nki.t1.controller;

import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.UserDto;
import com.nki.t1.exception.InvalidUserException;
import com.nki.t1.service.CustomUserDetailsService;
import com.nki.t1.service.MailSendService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

// 인증 (로그인/로그아웃, 회원가입/탈퇴 등)
@Slf4j
@RequestMapping("/auth")
@Controller
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final SessionUtils sessionUtils;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;

    public AuthController (CustomUserDetailsService customUserDetailsService, SessionUtils sessionUtils, PasswordEncoder passwordEncoder, MailSendService mailSendService) {
        this.userDetailsService = customUserDetailsService;
        this.sessionUtils = sessionUtils;
        this.passwordEncoder = passwordEncoder;
        this.mailSendService = mailSendService;
    }

    // 회원가입 폼
    @GetMapping("/signup")
    public String signupPage(HttpServletRequest request, Model model) {
        sessionUtils.setRefererURL(request);
        model.addAttribute("userDto", new UserDto());
        return "root.signup";
    }

    // 회원정보 변경용 로그인 정보 확인
    @PostMapping("/doubleLogin")
    public String doubleLogin(UserDto newUserDto, HttpServletRequest request) {
        // 로그인이 안되어있을 경우 - 로그인 페이지
        try {
            UserDto sessionUserDto = sessionUtils.getUserDto();
            String url = "/";

            sessionUserDto = userDetailsService.getUserDto(sessionUserDto.getUno());

            if(sessionUserDto.getEmail().equals(newUserDto.getEmail()) && passwordEncoder.matches(newUserDto.getPassword(), sessionUserDto.getPassword())) {
                // 로그인이 되어 있고, 입력값과 일치하는 경우 - 요청한 페이지
                url = sessionUtils.getRedirectURI(request);
                System.out.println("----- url1: " + url);

                // 요청한 페이지가 /login 이거나, /signup 이라면, 기본페이지
                if(url.contains("/login") || url.contains("/signup")) {
                    url = "/";
                    System.out.println("----- url2: " + url);
                }

            } else { // 로그인이 되어 있고, 입력값과 불일치하는 경우 - 이전 페이지
                log.error("Unmatched user info.");
                throw new InvalidUserException(ErrorType.USER_UNMATCHED, newUserDto);
            }
            System.out.println("----- url4: " + url);

            return "redirect:" + url;
        } catch (DataAccessException e) {
            throw new InvalidUserException(ErrorType.USER_UNMATCHED, newUserDto);
        } catch (InvalidUserException e) {
            log.error("Unexpected error occurred while doublelogin: " + e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while doublelogin: " + e.getMessage());
            throw new InvalidUserException(ErrorType.REQUEST_FAILED, newUserDto);
        }

    }

    // 아이디 중복 체크
    @PostMapping("/check-id")
    public ResponseEntity<String> checkId(String id) {
        if(userDetailsService.idChk(id) != 0) {
            return ResponseEntity.ok("Unvail");
        }
        return ResponseEntity.ok("Avail");
    }

    // 이메일 중복 체크
    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(String email) {
        if(userDetailsService.emailChk(email) != 0) {
            return ResponseEntity.ok("Unvail");
        }
        return ResponseEntity.ok("Avail");
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String processSignup(@Valid UserDto user, BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "root.signup";
        }

        Boolean isEmailAuthed = (Boolean) session.getAttribute("emailAuth");

        if(isEmailAuthed == null || !isEmailAuthed) {
            throw new InvalidUserException(ErrorType.USER_MAIL_UNAUTH, user);
        }

        userDetailsService.register(user, request);

        String previousURL = sessionUtils.getRefererURL(request);
        return "redirect:" + previousURL;
    }

    // 로그인 폼
    @RequestMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        sessionUtils.setRefererURL(request);

        UserDto userDto = (UserDto) request.getAttribute("userDto");
        ErrorResponse errorResponse = (ErrorResponse) request.getAttribute("error");
        if(userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        if(errorResponse != null) {
            model.addAttribute("error", errorResponse);
        }
        return "root.login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 종료
        request.getSession(false).invalidate();
        return "root.logout";
    }

    // 비밀번호 찾기 폼
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "root.findPw";
    }

    // 비밀번호 리셋
    @PostMapping("/reset-password")
    public String resetPassword(String email, RedirectAttributes reAttr, Model model) {
        UserDto userDto = userDetailsService.getUserDtoByEmail(email.replace("\\s", ""));
        // 가입 내역이 없는 경우
        if(userDto == null) {
            reAttr.addFlashAttribute("email", email);
            reAttr.addFlashAttribute("message", "가입내역이 없습니다.");
            return "redirect:/auth/reset-password";
        } else if (userDto.isSocial()) {
            reAttr.addFlashAttribute("message", "소셜 로그인 사용자입니다. 소셜 로그인을 이용해주세요.");
            return "redirect:/auth/login";
        }

        mailSendService.sendResetPwMailForm(email);

        model.addAttribute("message", "이메일로 전송된 임시 비밀번호를 통해 로그인하세요.");
        UserDto returnUserDto = new UserDto();
        returnUserDto.setEmail(email);
        model.addAttribute("userDto", returnUserDto);

        return "root.login";
    }

    // 회원 탈퇴
    @RequestMapping("/deactivate")
    public String deactivate(HttpServletRequest request, RedirectAttributes reAttr) {

        userDetailsService.deactivate(request);
        reAttr.addFlashAttribute("message", "탈퇴되었습니다.");
        return "redirect:/";
    }
}
