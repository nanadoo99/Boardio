package com.nki.t1.controller;

import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.ReportedCommentsDto;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.CommentService;
import com.nki.t1.service.ReportedCommentsService;
import com.nki.t1.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 사용자 페이지 - 게시글 댓글
@RestController
@RequestMapping("/user/comments")
public class UserCommentController {

    private static final Logger log = LoggerFactory.getLogger(UserCommentController.class);
    private CommentService commentService;
    private SessionUtils sessionUtils;
    private ReportedCommentsService reportedCommentsService;

    @Autowired
    public UserCommentController(CommentService commentService, SessionUtils sessionUtils, ReportedCommentsService reportedCommentsService) {
        this.commentService = commentService;
        this.sessionUtils = sessionUtils;
        this.reportedCommentsService = reportedCommentsService;
    }

    // 특정 게시글 댓글 불러오기
    @GetMapping("/posts/{pno}")
    public ResponseEntity<Map<String, Object>> readComments(@PathVariable Integer pno) {
        HashMap<String, Object> map = new HashMap<>();
        UserDto userDto = sessionUtils.getUserDto();

        CommentDto commentDto = new CommentDto();
        commentDto.setPno(pno);
        commentDto.setUno(userDto.getUno());

        List<CommentDto> comments = commentService.readComments(commentDto);
        map.put("comments", comments);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 작성 (@Valid > GlobalExceptionHandler.handleValidationExceptions 에서 처리)
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentDto commentDto) {
        UserDto userDto = sessionUtils.getUserDto();
        HashMap<String, Object> map = new HashMap<>();

        commentDto.setUno(userDto.getUno());
        commentService.insertComment(commentDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 수정 (@Valid > GlobalExceptionHandler.handleValidationExceptions 에서 처리)
    @PatchMapping("/{cno}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable("cno") Integer cno, @Valid @RequestBody CommentDto commentDto) {

        UserDto userDto = sessionUtils.getUserDto();
        HashMap<String, Object> map = new HashMap<>();

        commentDto.setUno(userDto.getUno());
        commentService.updateComment(commentDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{cno}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("cno") Integer cno) {
        UserDto userDto = sessionUtils.getUserDto();
        Map<String, Object> map = new HashMap<>();

        map.put("cno", cno);
        map.put("uno", userDto.getUno());

        commentService.deleteComment(map);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 신고
    @PostMapping("/{cno}/report")
    public ResponseEntity<Map<String, Object>> report(@PathVariable("cno") Integer cno, @ModelAttribute ReportedCommentsDto reportedCommentsDto) {
        UserDto userDto = sessionUtils.getUserDto();
        reportedCommentsDto.setReportUno(userDto.getUno());
        Map map = new HashMap<>();

        reportedCommentsService.reportByUnoCno(reportedCommentsDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 댓글 신고 취소
    @DeleteMapping("/{cno}/report")
    public ResponseEntity<Map<String, Object>> deleteReport(@PathVariable Integer cno) {
        UserDto userDto = sessionUtils.getUserDto();
        ReportedCommentsDto reportedCommentsDto = new ReportedCommentsDto(cno, userDto.getUno(), userDto.getUno());
        Map map = new HashMap<>();

        reportedCommentsService.deleteReportByUnoCno(reportedCommentsDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
