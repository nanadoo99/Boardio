package com.nki.t1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 사용자 페이지 - 마이 페이지
@Controller
@RequestMapping("/user/mypage")
public class UserMypageController {

    @GetMapping("")
    public String mypage() {
        return "user.mypage.mypage";
    }
}
