package com.nki.t1.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public String getData(String key){//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
        return (String) redisTemplate.opsForValue().get(key);
    }
    public void setData(String key,String value){//지정된 키(key)에 값을 저장하는 메서드
        redisTemplate.opsForValue().set(key, value);
    }
    public void setDataExpireMinutes(String key, String value, long duration){//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.MINUTES);
    }
    public void setDataExpireDays(String key, String value, long duration){//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.DAYS);
    }

    public boolean hasKey(String key){
        return redisTemplate.opsForValue().getOperations().hasKey(key);
    }
    public void deleteData(String key){//지정된 키(key)에 해당하는 데이터를 Redis에서 삭제하는 메서드
        redisTemplate.delete(key);
    }

}
