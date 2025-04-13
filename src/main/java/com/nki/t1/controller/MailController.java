package com.nki.t1.controller;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.EmailCheckDto;
import com.nki.t1.dto.EmailRequestDto;
import com.nki.t1.exception.InvalidEmailException;
import com.nki.t1.service.CustomUserDetailsService;
import com.nki.t1.service.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

// 메일
@Slf4j
@RequestMapping ("/auth/signup/mail")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailSendService mailService;
    private final CustomUserDetailsService userDetailsService;

    // 가입 인증 메일을 보낸다
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendAuthMail(@RequestBody EmailRequestDto emailRequestDto) {
        log.info("이메일 인증 이메일 :"+ emailRequestDto.getEmail());
        if(userDetailsService.emailChk(emailRequestDto.getEmail()) != 0) {
            throw new InvalidEmailException(ErrorType.USER_REG_EXIST);
        }
        mailService.sendJoinMailForm(emailRequestDto.getEmail());
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    // 인증 코드를 확인한다.
    @PostMapping("/verify")
    public String verifyAuthCode(@RequestBody EmailCheckDto emailCheckDto, HttpSession session) {
        Boolean isAuthNumChecked = mailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if (isAuthNumChecked) {
            session.setAttribute("emailAuth", true);
            return "ok";
        } else {
            throw new InvalidEmailException(ErrorType.EMAIL_);
        }
    }
}
