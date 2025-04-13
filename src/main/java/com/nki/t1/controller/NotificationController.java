package com.nki.t1.controller;

import com.nki.t1.dto.NotificationDto;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.service.NotificationService;
import com.nki.t1.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 사용자 - 알림
@Controller
@RequestMapping("/user/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final SessionUtils sessionUtils;

    @Autowired
    public NotificationController(NotificationService notificationService, SessionUtils sessionUtils) {
        this.notificationService = notificationService;
        this.sessionUtils = sessionUtils;
    }

    // 알림 수
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countNotifications() {
        UserSecurityDto userSecurityDto =  sessionUtils.getUserSecurityDto();
        int count = notificationService.getNotificationCount(userSecurityDto);

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 알림 데이터 리스트
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNotificationList() {
        HashMap<String, Object> map = new HashMap<>();
        List<NotificationDto> list = notificationService.getNotificationList();

        map.put("list", list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 알림 클릭시 이동
    @GetMapping("/{id}")
    public String clickNotification(@PathVariable Integer id) {
        // 같은 type 과 targetParentIdx를 가진 알림을 지우고,
        NotificationDto notificationDto = notificationService.readNotification(id);
        // type 에 맞는 주소로 redirect 한다.
        return "redirect:" + notificationDto.getTargetUri();
    }

}
