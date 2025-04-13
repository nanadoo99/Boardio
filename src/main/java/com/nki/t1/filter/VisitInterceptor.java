/*
package com.nki.t1.filter;

import com.nki.t1.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Slf4j
@Component
public class VisitInterceptor implements HandlerInterceptor {
    @Value("@{visitor.count.redis.key.prefix}")
    private String prefix;

    private final RedisUtil redisUtil;

    public VisitInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String today = LocalDate.now().toString();
        String key = prefix + userIp + "_" + today;

        if(!redisUtil.hasKey(key)) {
            redisUtil.setData(key, userAgent);
        }

        log.info("----- VisitInterceptor.preHandle -----");
        return true;
    }

}*/
