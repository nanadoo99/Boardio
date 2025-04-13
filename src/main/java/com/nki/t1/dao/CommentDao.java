package com.nki.t1.dao;

import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;
import java.util.Map;

public interface CommentDao {

    int countCommentByAdmin(SearchCondition sc);
    List<CommentDto> selectCommentPageByAdmin(SearchCondition sc);
    List<CommentDto> selectCommentByAdminPno(SearchCondition sc);
    List<CommentDto> selectCommentPageByAdminUno(SearchCondition sc);

    CommentDto selectCommentByCno(Integer cno);
    List<CommentDto> selectComments(CommentDto commentDto);
    int insertComment(CommentDto commentDto);
    int updateComment(CommentDto commentDto);
    int deleteComment(Map map);
    int deleteAll(int pno);

    int countCommentsByUno(SearchCondition sc);
    List<CommentDto> getCommentPageByUno(SearchCondition sc);
}
