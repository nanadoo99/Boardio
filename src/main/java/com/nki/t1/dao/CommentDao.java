package com.nki.t1.dao;

import com.nki.t1.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentDao {
    int countComments(int pno);

    List<CommentDto> selectComments(int pno);

    int insertComment(CommentDto commentDto);

    int updateComment(CommentDto commentDto);

    int deleteComment(Map map);

    int deleteAll(int pno);
}
