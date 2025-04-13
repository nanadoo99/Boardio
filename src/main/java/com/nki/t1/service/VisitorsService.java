package com.nki.t1.service;

import com.nki.t1.dao.VisitorsDao;
import com.nki.t1.dto.VisitorDto;
import com.nki.t1.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.StringTokenizer;

@Slf4j
@Service
public class VisitorsService {
    @Value("${visitor.count.redis.key.prefix}")
    private String prefix;
    @Value("${visitor.count.redis.key.delimiter}")
    private String delimiter;
    @Value("${visitor.count.redis.update.dateTime.key}")
    private String visitorUpdateDateTime;
    private final VisitorsDao visitorsDao;
    private final RedisUtil redisUtil;

    public VisitorsService(VisitorsDao visitorsDao, RedisUtil redisUtil) {
        this.visitorsDao = visitorsDao;
        this.redisUtil = redisUtil;
    }

//    @Scheduled(initialDelay = 3000000, fixedDelay = 3000000)
    @Scheduled(fixedRate = 5000)
    public void updateVistorsDb() {

        updateVisitorTime();

        Set<String> keys = redisUtil.getKeys(prefix + "*");

        for(String key : keys) {
            StringTokenizer tokenizer = new StringTokenizer(key, delimiter);
            tokenizer.nextToken();
            log.info("----- VisitorsService.updateVistorsDb -----");
            log.info("----- key: {}", key);

            VisitorDto visitorDto = new VisitorDto.Builder()
                    .uno(Integer.parseInt(tokenizer.nextToken()))
                    .date(LocalDate.parse(tokenizer.nextToken()))
                    .build();

            if(!visitorsDao.existsByUserIpAndDate(visitorDto)) {
                visitorsDao.insertVisitor(visitorDto);
            }

            redisUtil.deleteData(key);

        }
    }

    public VisitorDto countVisitorsToday() {
        return visitorsDao.countVisitorsToday();
    }

    public VisitorDto countVisitorsTotal() {
        return visitorsDao.countVisitorsTotal();
    }

    // 방문자수 집계 기준 시각을 업데이트 한다.
    private void updateVisitorTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        redisUtil.setData(visitorUpdateDateTime, formattedDate);
    }
}
