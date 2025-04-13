package com.nki.t1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 관리자 페이지 진입
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String admin() {
        return "admin.main.adminHome";
    }

}