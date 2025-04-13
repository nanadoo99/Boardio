package com.nki.t1.controller;

import com.nki.t1.service.VisitorsService;
import com.nki.t1.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

// 관리자 페이지 - 방문자
@Controller
@RequestMapping("/admin/visitors")
public class AdminVisitorController {

    @Value("${visitor.count.redis.update.dateTime.key}")
    private String visitorUpdateDateTime;

    private final VisitorsService visitorsService;
    private final RedisUtil redisUtil;

    public AdminVisitorController(VisitorsService visitorsService, RedisUtil redisUtil) {
        this.visitorsService = visitorsService;
        this.redisUtil = redisUtil;
    }

    // 방문자 수 조회
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map> count(){
        Map<String, Object> map = new HashMap<>();

        visitorsService.updateVistorsDb();
        map.put("today", visitorsService.countVisitorsToday());
        map.put("total", visitorsService.countVisitorsTotal());
        map.put("updateDateTime", redisUtil.getData(visitorUpdateDateTime));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}