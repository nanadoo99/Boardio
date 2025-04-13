package com.nki.t1.controller;

import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.Role;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.*;
import com.nki.t1.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

// 관리자 페이지 - 유저 관리
@Slf4j
@Controller
@RequestMapping("/admin/user-management")
public class AdminUserManagementController {

    private final UserService userService;
    private final PostService postService;;
    private final CommentService commentService;
    private final CustomUserDetailsService customUserDetailsService;
    private final MessageSource messageSource;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> ADMIN_USERINFO_PATH = Url.getPath(Url.ADMIN_USERINFO);

    public AdminUserManagementController(UserService userService,
                                         PostService postService,
                                         CommentService commentService,
                                         CustomUserDetailsService customUserDetailsService,
                                         MessageSource messageSource) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.customUserDetailsService = customUserDetailsService;
        this.messageSource = messageSource;
    }

    // 사용자 관리 페이지 진입
    @GetMapping("")
    public String getAdminUserPage(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_USERINFO_PATH);

        return "admin.userInfo.adminUserList";
    }

    // 사용자 목록 조회
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUsers(@ModelAttribute @Valid SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> map = new HashMap<>();
        int totalCnt = userService.countUserByAdmin(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<UserAdminDto> list = userService.selectUserListByAdmin(sc);

        map.put("list", list);
        map.put("ph", pageHandler);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 사용자 정보 조회
    @GetMapping("/{uno}")
    public String getUserDetailPage(@PathVariable("uno") Integer uno, @ModelAttribute SearchCondition sc, Model model) {
        int postPageSize = 5;
        int commentPageSize = 5;

        // 사용자 정보
        UserAdminDto userAdminDto = userService.selectUserByAdmin(uno);

        // 작성 게시글
        List<PostDto> postDtoList =  postService.selectPostPageByAdminUno(new SearchCondition.Builder().pageSize(postPageSize).uno(uno).option3("entirePeriod").build());

        // 작성 댓글
        List<CommentDto> commentDtoList =  commentService.selectCommentPageByAdminUno(new SearchCondition.Builder().pageSize(commentPageSize).uno(uno).build());

        // 권한 리스트
        List<Role> roleList = Arrays.stream(Role.values())
                                .filter(role -> !role.equals(Role.SUPER_ADMIN))
                                .collect(Collectors.toList());

        model.addAttribute("sc", sc);
        model.addAttribute("uno", uno);
        model.addAttribute("userAdminDto", userAdminDto);
        model.addAttribute("postDtoList", postDtoList);
        model.addAttribute("commentDtoList", commentDtoList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("postPageSize", postPageSize);
        model.addAttribute("commentPageSize", commentPageSize);

        return "admin.userInfo.adminUserDetail";
    }

    // 사용자 잠금 해제
    @DeleteMapping("/{uno}/lock")
    @ResponseBody
    public ResponseEntity<Map> unlockUser(@PathVariable("uno") Integer uno) {
        customUserDetailsService.resetFailures(uno);
        Map<String, Object> map = new HashMap<>();

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}