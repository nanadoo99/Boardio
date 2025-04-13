package com.nki.t1.controller;

import com.nki.t1.dto.*;
import com.nki.t1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// 최고 관리자 - 권한 변경
@Slf4j
@Controller
@RequestMapping("/superAdmin/user-management")
public class SuperAdminUserController {

    private final UserService userService;

    public SuperAdminUserController(UserService userService) {
        this.userService = userService;
    }

    // 권한변경
    @PostMapping("/updateRole")
    @ResponseBody
    public ResponseEntity<Map> updateUserRole(@RequestBody UserDto userDto) {
        userService.updateUserRoleBySuperAdmin(userDto);
        Map<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}