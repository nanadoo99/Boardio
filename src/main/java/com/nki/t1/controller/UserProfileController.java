package com.nki.t1.controller;

import com.nki.t1.domain.Url;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.CustomUserDetailsService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/user/profile")
public class UserProfileController {
    private final SessionUtils sessionUtils;
//    private AuthService authService;
    private final CustomUserDetailsService userDetailsService;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> USER_PROFILE_PATH = Url.getPath(Url.USER_MYPAGE_PROFILE);


    public UserProfileController(SessionUtils sessionUtil, CustomUserDetailsService userDetailsService) {
        this.sessionUtils = sessionUtil;
        this.userDetailsService = userDetailsService;
    }

    // 마이페이지 진입시 로그인 정보 재입력
    @GetMapping
    public String show(HttpServletRequest request) {
        UserDto userDto = sessionUtils.getUserDto();

        if(userDto.isSocial()) { // 소셜 로그인 계정인 경우 바로 이동
            return "forward:/user/profile/enter";
        }

        // 일반 로그인 계정 - 로그인 정보 재확인
        sessionUtils.setRefererURL(request); // 실패시 돌아가는 페이지. 검토: AuthController 에서 처리
        sessionUtils.setRedirectURI(request, "/user/profile/enter");
        return  "root.doubleLogin";
    }

    // 마이페이지 진입
    @GetMapping("/enter")
    public String enter(Model model) {
        model.addAttribute("mode", "profile");
        model.addAttribute(PAGE_PATH, USER_PROFILE_PATH);
        return  "user.mypage.profile";
    }

    // 회원정보 업데이트
    @PostMapping("/{id}")
    public String processSignup(@PathVariable("id") String id, UserDto user, HttpServletRequest request, Model model, RedirectAttributes reAttr) {
        userDetailsService.updateUser(user, request);

        reAttr.addFlashAttribute("message", "회원정보가 성공적으로 수정되었습니다.");
        return "redirect:/";
    }

}
