package com.nki.t1.controller;

import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.*;
import com.nki.t1.service.BlockReasonService;
import com.nki.t1.service.BlockedPostService;
import com.nki.t1.service.PostService;
import com.nki.t1.service.ReportedPostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// 관리자 페이지 - 게시글 관리
@Slf4j
@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final PostService postService;
    private final ReportedPostsService reportedPostsService;
    private final BlockedPostService blockedPostService;
    private final BlockReasonService blockReasonService;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> ADMIN_POST_PATH = Url.getPath(Url.ADMIN_POST);
    private final MessageSource messageSource;

    public AdminPostController(PostService postService,
                               ReportedPostsService reportedPostsService,
                               BlockedPostService blockedPostService,
                               BlockReasonService blockReasonService,
                               MessageSource messageSource) {
        this.postService = postService;
        this.reportedPostsService = reportedPostsService;
        this.blockedPostService = blockedPostService;
        this.blockReasonService = blockReasonService;
        this.messageSource = messageSource;
    }

    // 페이지 진입
    @GetMapping("")
    public String getAdminPostPage(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_POST_PATH);

        return "admin.post.adminPostList";
    }

    // 게시글 목록
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPosts(@Valid @ModelAttribute SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Object> map = new HashMap<>();
        int totalCnt = postService.countPostByAdmin(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<PostDto> list = postService.selectPostPageByAdmin(sc);

        map.put("list", list);
        map.put("ph", pageHandler);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 게시글 조회 - 페이지 진입
    @GetMapping("/{pno}")
    public String getPostPage(@PathVariable Integer pno, @ModelAttribute SearchCondition sc, Model model, HttpServletRequest request) {
        log.info("----- requestURI: " + request.getRequestURI());
        log.info("----- getHeader: " + request.getHeader("Referer"));

        // org: 목록으로 돌아가기 위함(게시글관리 혹은 댓글관리 중 선택)
        String org = "posts";
        String referer = request.getHeader("Referer");
        if (referer.contains("comment")) {
            org = "comments";
        }
        model.addAttribute("org", org);

        List<BlockReasonsDto> brList = blockReasonService.selectBlockReasons();
        model.addAttribute("sc", sc);
        model.addAttribute("pno", pno);
        model.addAttribute("brList", brList);

        return "admin.post.adminPostRead";
    }

    // 특정 게시글 조회 - 게시글 데이터 불러오기
    @GetMapping("/{pno}/data")
    @ResponseBody
    public ResponseEntity<Map> getPostData(@PathVariable Integer pno) {
        HashMap<String, Object> map = new HashMap<>();
        PostDto postDto = postService.getPostWithAuth(pno);
        List<BlockedPostDto> bpList = blockedPostService.selectBlocksByPno(pno);

        map.put("postDto", postDto);
        map.put("bpList", bpList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 게시글 신고 내역
    @GetMapping("/{pno}/reports")
    @ResponseBody
    public ResponseEntity<Map> getPostReports(@PathVariable Integer pno) {
        HashMap<String, Object> map = new HashMap<>();
        SearchCondition sc = new SearchCondition();
        sc.setIdx1(pno);
        List<ReportedPostsDto> reportList = reportedPostsService.getReportsPage(sc);
        List<ReportedPostsDto> reportSummary = reportedPostsService.getReportsGroupByBrno(sc);
        Integer reportCount = reportSummary.stream().mapToInt(ReportedPostsDto::getBrnoCnt).sum();

        map.put("reportList", reportList);
        map.put("reportSummary", reportSummary);
        map.put("reportCount", reportCount);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 게시글 차단
    @PostMapping("/{pno}/block")
    @ResponseBody
    public ResponseEntity<Map> blockPost(@PathVariable("pno") Integer pno, @ModelAttribute BlockedPostDto blockedPostDto) {
        HashMap<String, Object> map = new HashMap<>();
        blockedPostService.blockPost(blockedPostDto);

        PostDto postDto = postService.getPostWithAuth(blockedPostDto.getPno());
        map.put("postDto", postDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 게시글 차단 해제
    @DeleteMapping("/{pno}/block")
    @ResponseBody
    public ResponseEntity<Map> unblockPost(@PathVariable("pno") Integer pno, @RequestBody BlockedPostDto blockedPostDto) {
        HashMap<String, Object> map = new HashMap<>();
        blockedPostService.deleteBlockedPost(blockedPostDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}