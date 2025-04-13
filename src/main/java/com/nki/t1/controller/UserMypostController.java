package com.nki.t1.controller;

import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.BlockReasonsDto;
import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import com.nki.t1.exception.BadRequestException;
import com.nki.t1.security.CustomAuthenticationFailureHandler;
import com.nki.t1.service.BlockReasonService;
import com.nki.t1.service.PostService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// 사용자 페이지 - 작성 게시글
@Slf4j
@Controller
@RequestMapping("/user/mypost")
public class UserMypostController {
    private final PostService postService;
    private final BlockReasonService blockReasonService;
    private final SessionUtils sessionUtils;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> USER_MYPOST_PATH = Url.getPath(Url.USER_MYPAGE_POST);

    public UserMypostController(PostService postService,
                                BlockReasonService blockReasonService,
                                SessionUtils sessionUtils) {
        this.postService = postService;
        this.blockReasonService = blockReasonService;
        this.sessionUtils = sessionUtils;
    }

    // 게시물 리스트
    @GetMapping("/list")
    public String list(@ModelAttribute SearchCondition sc, Model model) {
        UserDto userDto = sessionUtils.getUserDto();
        sc.setUno(userDto.getUno());
        sc.setCode1(99); // 차단된 게시글 포함

        int totalCnt = postService.countMyPost(sc);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<PostDto> postList = postService.selectMyPostPage(sc);

        model.addAttribute("postList", postList);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, USER_MYPOST_PATH);

        return "user.mypage.mypostList";

    }

    // 게시글 수정
    @GetMapping("/edit/{pno}")
    public String upload(@PathVariable String pno) {
        return "forward:/user/post/postEdit/" + pno;
    }


    // 게시글 삭제
    @GetMapping("/delete/{pno}")
    public String delete(@PathVariable String pno) {
        return "forward:/user/post/postDelete/" + pno;
    }


    // 게시물 읽기
    @GetMapping("/read/{pno}")
    public String read(@PathVariable("pno") Integer pno, Model model, RedirectAttributes reAttr) {
        PostDto postDto = null;
        String errorRedirectUrl = "/user/mypost/list";

        try {
            postDto = postService.getPostWithAuth(pno);
            List<BlockReasonsDto> brList = blockReasonService.selectBlockReasons();

            model.addAttribute("postDto", postDto);
            model.addAttribute("brList", brList);

            return "user.mypage.mypost";
        } catch (BadRequestException e) {
            log.error("BadRequestException while reading mypost: ", e);
            reAttr.addFlashAttribute(new ErrorResponse(e.getErrorType()));

            return "redirect:" + errorRedirectUrl;
        } catch (Exception e) {
            log.error("RuntimeException while reading mypost: ", e);
            reAttr.addFlashAttribute(new ErrorResponse(ErrorType.REQUEST_FAILED));

            return "redirect:" + errorRedirectUrl;
        }

    }

}
