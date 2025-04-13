package com.nki.t1.controller;

import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.BannerDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.service.AnnounceService;
import com.nki.t1.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;

// 홈
@Slf4j
@Controller
public class Home {

    private final Logger logger = LogManager.getLogger(Home.class);

    private final BannerService bannerService;
    private final AnnounceService announceService;

    public Home(BannerService bannerService, AnnounceService announceService) {
        this.bannerService = bannerService;
        this.announceService = announceService;
    }

    @GetMapping("/")
    public String home(Model model) {
        showAuth();

        // 배너를 가져온다. (디폴트 배너(id = 0)도 포함)
        List<BannerDto> bannerDtoList = bannerService.getBannerList();
        log.info("------ home bannerDtoList: {}", bannerDtoList);

        // 최근 공지사항을 가져온다.
        SearchCondition sc = new SearchCondition(5);
        List<AnnounceDto> announceDtoList = announceService.getAnnouncePageUser(sc);

        model.addAttribute("bannerDtoList", bannerDtoList);
        model.addAttribute("announceDtoList", announceDtoList);

        return "root.index";
    }

    private void showAuth() {
        // 현재 인증된 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 사용자의 권한을 확인
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            log.info("User has role: " + role);
        }
    }

}
