package com.nki.t1.controller;

import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 불러오기
    @GetMapping
    public ResponseEntity<List<CommentDto>> readComments(@RequestParam Integer pno) {
        try {
            List<CommentDto> list = commentService.readComments(pno);
            return new ResponseEntity<>(list, HttpStatus.OK);  // 200
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400
        }
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        System.out.println("@@@@@ CommentController.createComment");
        try {
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            commentDto.setUno(userDto.getUno());

            if (commentService.insertComment(commentDto) != 1)
                throw new Exception("creating comment failed");
            return new ResponseEntity<>("CREATE_FAILED", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("CREATE_SUCCEEDED", HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글 수정
    @PatchMapping
    public ResponseEntity<String> updateComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            commentDto.setUno(userDto.getUno());

            if(commentService.updateComment(commentDto) != 1)
                throw new Exception("updating comment failed");
            return new ResponseEntity<>("UPDATE_FAILED", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("UPDATE_SUCCEEDED", HttpStatus.BAD_REQUEST);
        }

    }
    // 댓글 삭제
    @DeleteMapping("/{cno}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer cno, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            Map<String, Object> map = new HashMap<>();
            map.put("cno", cno);
            map.put("uno", userDto.getUno());

            if(commentService.deleteComment(map) != 1)
                throw new Exception("deleting comment failed");
            return new ResponseEntity<>("DELETE_FAILED", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DELETE_SUCCEEDED", HttpStatus.BAD_REQUEST);
        }
    }
}
