package com.nki.t1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // 이메일 전송에 사용할 SMTP 서버 호스트
        mailSender.setPort(587); // 메일 포트
        mailSender.setUsername("kyungin0528@gmail.com");
        mailSender.setPassword("hkep rrgd njqm npyn");

        Properties javaMailProperties = new Properties(); // 속성
        javaMailProperties.put("mail.transport.protocol", "smtp");//프로토콜로 smtp 사용
        javaMailProperties.put("mail.smtp.auth", "true");//smtp 서버에 인증이 필요
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//SSL 소켓 팩토리 클래스 사용
        javaMailProperties.put("mail.smtp.starttls.enable", "true");//STARTTLS(TLS를 시작하는 명령)를 사용하여 암호화된 통신을 활성화
        javaMailProperties.put("mail.debug", "true");//디버깅 정보 출력
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");//smtp 서버의 ssl 인증서를 신뢰
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");//사용할 ssl 프로토콜 버젼

        mailSender.setJavaMailProperties(javaMailProperties); // 속성 대입

        return mailSender; // 빈으로 등록

    }
}
