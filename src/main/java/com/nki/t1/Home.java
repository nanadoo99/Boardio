package com.nki.t1;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class Home {
    @GetMapping("/")
    public String home() {
        System.out.println("@@@ Help me@");
        showAuth();
        return "index";
    }

    private void showAuth() {
        // 현재 인증된 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 사용자의 권한을 확인
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            System.out.println("User has role: " + role);
        }
    }

}
