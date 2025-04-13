package com.nki.t1.security;

import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.service.NotificationService;
import com.nki.t1.utils.RedisUtil;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${visitor.count.redis.key.prefix}")
    private String visitorPrefix;
    @Value("${visitor.count.redis.key.delimiter}")
    private String delimiter;

    @Value("${notifiation.redis.key.prefix}")
    private String notificationPrefix;
    @Value("${notifiation.count.redis.key.prefix}")
    private String notificationCountPrefix;

    private final RedisUtil redisUtil;
    
    public CustomAuthenticationSuccessHandler(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        SessionUtils sessionUtils = new SessionUtils();
        String refererURL = sessionUtils.getRefererURL(request);
        String redirectURI = sessionUtils.getRedirectURI(request);

        // refererURL.contains("/profile") > redirectURI 사용
        log.info("----- refererURL: " + refererURL);
        log.info("----- redirectURI: " + redirectURI);

        if (refererURL != null) {
            if(refererURL.contains("/profile")) {
                getRedirectStrategy().sendRedirect(request, response, redirectURI);
            } else {
                getRedirectStrategy().sendRedirect(request, response, refererURL);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

        setUserInSession(request, authentication);
    }

    public void setUserInSession(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserSecurityDto) {
            UserSecurityDto userSecurityDto = (UserSecurityDto) principal;
            String userEmail = userSecurityDto.getEmail();
            HttpSession session = request.getSession();
            if (userEmail != null || !userEmail.isEmpty()) {
                // 세션 등록
                session.setAttribute("authUser", userSecurityDto);

            }
            String ipHost = "";

            try {
                InetAddress ip = InetAddress.getLocalHost();
                ipHost =ip.getHostName() + "/"+ ip.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            session.setAttribute("ssReqIp", ipHost);
            // 방문 기록 등록
            saveVistorInRedis(userSecurityDto.getUno());

        }
    }

    // 방문 기록 확인 메소드
    // redis에 저장
    private void saveVistorInRedis(Integer uno) {
        String userIdx = Integer.toString(uno);
        String today = LocalDate.now().toString();
        String key = String.join(delimiter, visitorPrefix, userIdx, today);

        if(!redisUtil.hasKey(key)) {
            redisUtil.setDataExpireDays(key, "1", 1);
        }

        log.info("----- CustomAuthenticationSuccessHandler.saveVistorInRedis -----");
        log.info("----- key = {}", key);
    }
}
