package com.nki.t1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class NewController {
    @GetMapping("/admin")
    public String admin() {
        // this is a change for git
        return "admin";
    }
}