package com.nki.t1.controller;

import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.CommentService;
import com.nki.t1.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 사용자 페이지 - 작성 댓글
@Controller
@RequestMapping("/user/mycomments")
public class UserMycommentController {
    private final CommentService commentService;
    private final SessionUtils sessionUtils;
    private final String PAGE_PATH = "pagePath";
    private final List<Url> USER_MYCOMMENT_PATH = Url.getPath(Url.USER_MYPAGE_COMMENT);

    public UserMycommentController(CommentService commentService, SessionUtils sessionUtils) {
        this.commentService = commentService;
        this.sessionUtils = sessionUtils;
    }

    // 댓글 리스트
    @GetMapping("/list")
    public String list(@ModelAttribute SearchCondition sc, Model model) {
//        UserDto userDto = sessionUtils.getUserDto(request);
        UserDto userDto = sessionUtils.getUserDto();
        sc.setUno(userDto.getUno());

        int totalCnt = commentService.countCommentsByUno(sc);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<CommentDto> commentList = commentService.getCommentPageByUno(sc);

        model.addAttribute("commentList", commentList);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, USER_MYCOMMENT_PATH);

        return "user.mypage.mycommentList";
    }
}
