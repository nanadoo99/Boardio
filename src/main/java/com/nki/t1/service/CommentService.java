package com.nki.t1.service;

import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;
import java.util.Map;

public interface CommentService {
    int countCommentByAdmin(SearchCondition sc);
    List<CommentDto> selectCommentPageByAdmin(SearchCondition sc);
    List<CommentDto> selectCommentByAdminPno(SearchCondition sc);
    List<CommentDto> selectCommentPageByAdminUno(SearchCondition sc);

    CommentDto selectCommentByCno(Integer cno);
    List<CommentDto> readComments(CommentDto commentDto);
    void insertComment(CommentDto commentDto);
    void updateComment(CommentDto commentDto);
    void deleteComment(Map map);

    int countCommentsByUno(SearchCondition sc);
    List<CommentDto> getCommentPageByUno(SearchCondition sc);
}
