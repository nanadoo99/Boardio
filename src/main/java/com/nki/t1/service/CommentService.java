package com.nki.t1.service;

import com.nki.t1.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentService {
    int countComments(int pno);

    List<CommentDto> readComments(int pno);

    List<CommentDto> selectComments(int pno);

    int insertComment(CommentDto commentDto);

    int updateComment(CommentDto commentDto);

    int deleteComment(Map map);
}
