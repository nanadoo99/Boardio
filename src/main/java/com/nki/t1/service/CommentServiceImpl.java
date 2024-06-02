package com.nki.t1.service;

import com.nki.t1.dao.CommentDao;
import com.nki.t1.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public int countComments(int pno) {
        return commentDao.countComments(pno);
    }

    @Override
    public List<CommentDto> readComments(int pno) {
        return commentDao.selectComments(pno);
    }

    @Override
    public List<CommentDto> selectComments(int pno) {
        return commentDao.selectComments(pno);
    }

    @Override
    public int insertComment(CommentDto commentDto) {
        return commentDao.insertComment(commentDto);
    }

    @Override
    public int updateComment(CommentDto commentDto) {
        return commentDao.updateComment(commentDto);
    }

    @Override
    public int deleteComment(Map map) {
        return commentDao.deleteComment(map);
    }
}
