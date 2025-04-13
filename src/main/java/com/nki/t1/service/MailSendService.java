package com.nki.t1.service;

import com.nki.t1.dao.UserDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.UserDto;
import com.nki.t1.exception.InvalidEmailException;
import com.nki.t1.utils.RedisUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailSendService {;

    private int authNumber;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public MailSendService(JavaMailSender mailSender, RedisUtil redisUtil, PasswordEncoder passwordEncoder, UserDao userDao) {
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    private void generateRandomNumber() {
        Random rand = new Random();
        authNumber = rand.nextInt(900000) + 100000;
    }

    public void sendJoinMailForm(String email) {
        generateRandomNumber();
        redisUtil.setDataExpireMinutes(Integer.toString(authNumber), email, 5);

        String from = "kyungin0528@gmail.com";
        String to = email;
        String title = "회원 가입 인증 이메일입니다.";
        String content = "MiniBoard 방문을 환영합니다." +
                "<br><br>" +
                "인증번호는" + authNumber + "입니다." +
                "<br>" +
                "인증번호를 올바르게 입력하세요";
        sendMail(from, to, title, content);
    }

    public void sendMail(String from, String to, String title, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8"); // 이메일 관련 설정
            // true: multipart 지원
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) { // 이메일 서버 연결이 안되거나, 잘못된 이메일 주소일 경우.
            e.printStackTrace();
            throw new InvalidEmailException(ErrorType.EMAIL_);

        }

    }

    public void sendResetPwMailForm(String email) {
        generateRandomNumber();

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(passwordEncoder.encode(String.valueOf(authNumber)));

        userDao.updatePwByEmail(userDto);

        String from = "kyungin0528@gmail.com";
        String to = email;
        String title = "임시 비밀번호입니다.";
        String content = "MiniBoard 방문을 환영합니다." +
                "<br><br>" +
                "임시 비밀번호는" + authNumber + "입니다." +
                "<br>";
        sendMail(from, to, title, content);
    }

    // 사용자가 입력한 인증번호와 실제 인증 번호 비교
    public boolean checkAuthNum(String email, String authNum) {
        String redisEmail = redisUtil.getData(authNum);
        if (redisEmail == null) {
            return false; // 인증번호가 Redis에 없음
        } else if (redisEmail.equals(email)) {
            redisUtil.deleteData(authNum); // 인증 성공 후 데이터 삭제
            return true;
        } else {
            return false; // 이메일과 인증번호가 일치하지 않음
        }
    }
}
