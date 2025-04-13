package com.nki.t1.controller;

import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.*;
import com.nki.t1.service.BlockedCommentService;
import com.nki.t1.service.CommentService;
import com.nki.t1.service.ReportedCommentsService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// 관리자 페이지 - 댓글 관리
@Slf4j
@Controller
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;
    private final BlockedCommentService blockedCommentService;
    private final ReportedCommentsService reportedCommentsService;
    private final SessionUtils sessionUtils;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> ADMIN_COMMENT_PATH = Url.getPath(Url.ADMIN_COMMENT);
    private final MessageSource messageSource;

    @Autowired
    public AdminCommentController(CommentService commentService,
                                  BlockedCommentService blockedCommentService,
                                  ReportedCommentsService reportedCommentsService,
                                  SessionUtils sessionUtils,
                                  MessageSource messageSource) {
        this.commentService = commentService;
        this.blockedCommentService = blockedCommentService;
        this.reportedCommentsService = reportedCommentsService;
        this.sessionUtils = sessionUtils;
        this.messageSource = messageSource;
    }

    // 댓글 관리 메뉴 - 진입
    @GetMapping("")
    public String admin(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_COMMENT_PATH);

        return "admin.post.adminCommentList";
    }

    // 댓글 관리 메뉴 - 댓글 목록
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getComments(@Valid @ModelAttribute SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        HashMap<String, Object> map = new HashMap<>();
        int totalCnt = commentService.countCommentByAdmin(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<CommentDto> list = commentService.selectCommentPageByAdmin(sc);

        map.put("list", list);
        map.put("ph", pageHandler);
        map.put("sc", sc);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/{pno}/comments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPostComments(@PathVariable Integer pno) {
        HashMap<String, Object> map = new HashMap<>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setIdx1(pno);

        List<CommentDto> comments = commentService.selectCommentByAdminPno(searchCondition);
        map.put("comments", comments);
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    // 댓글 차단
    @PostMapping("/{cno}/blockNew")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> blockComment(@PathVariable("cno") Integer cno, @ModelAttribute BlockedCommentDto blockedCommentDto) {
        blockedCommentService.blockComment(blockedCommentDto);
        Map<String, Object> map = new HashMap<>();
        map.put("cno", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 차단 사유 변경
    @PutMapping("/{cno}/blockUpdate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changeBlockReason(@PathVariable("cno") Integer cno, @RequestBody BlockedCommentDto blockedCommentDto) {
        blockedCommentDto.setUno(sessionUtils.getUserDto().getUno());
        blockedCommentService.blockComment(blockedCommentDto);

        Map<String, Object> map = new HashMap<>();
        map.put("cno", "success");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 신고 내역
    @PostMapping("/{cno}/reports")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCommentReportHistory(@PathVariable("cno") Integer cno) {
        Map<String, Object> map = new HashMap<>();
        SearchCondition searchCondition = new SearchCondition();

        searchCondition.setIdx1(cno);
        List<ReportedCommentsDto> reportList = reportedCommentsService.getReportsPage(searchCondition);
        List<ReportedCommentsDto> reportSummary = reportedCommentsService.getReportsGroupByBrno(searchCondition);

        map.put("reportList", reportList);
        map.put("reportSummary", reportSummary);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 차단 해제
    @DeleteMapping("/{cno}/block")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unblockComment(@PathVariable("cno") Integer cno) {
        BlockedCommentDto blockedCommentDto = new BlockedCommentDto();
        blockedCommentDto.setCno(cno);
        blockedCommentDto.setUno(sessionUtils.getUserDto().getUno());
        blockedCommentService.deleteBlockedComment(blockedCommentDto);
        Map<String, Object> map = new HashMap<>(); // exceptionHandler 의 반환 값에 맞춰줌

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}