package com.nki.t1.service;

import com.nki.t1.dao.CommentDao;
import com.nki.t1.dao.PostDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.exception.InvalidCommentException;
import com.nki.t1.exception.UnauthorizedException;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final SessionUtils sessionUtils;
    private final NotificationService notificationService;
    private final PostDao postDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao, SessionUtils sessionUtils, NotificationService notificationService, PostDao postDao) {
        this.commentDao = commentDao;
        this.sessionUtils = sessionUtils;
        this.notificationService = notificationService;
        this.postDao = postDao;
    }

    @Override
    public int countCommentByAdmin(SearchCondition sc) {
        return commentDao.countCommentByAdmin(sc);
    }

    @Override
    public List<CommentDto> selectCommentPageByAdmin(SearchCondition sc) {
        return commentDao.selectCommentPageByAdmin(sc);
    }

    @Override
    public List<CommentDto> selectCommentByAdminPno(SearchCondition sc) {
        return commentDao.selectCommentByAdminPno(sc);
    }

    @Override
    public List<CommentDto> selectCommentPageByAdminUno(SearchCondition sc) {
        return commentDao.selectCommentPageByAdminUno(sc);
    }

    @Override
    public CommentDto selectCommentByCno(Integer cno) {
        return commentDao.selectCommentByCno(cno);
    }

    @Override
    public List<CommentDto> readComments(CommentDto commentDto) {

        List<CommentDto> list = commentDao.selectComments(commentDto);
        return list;
    }

    @Override
    public void insertComment(CommentDto commentDto) throws InvalidCommentException {
        try {
            int result = commentDao.insertComment(commentDto);
            log.info("----- insert commentDto cno:{}", commentDto.getCno());
            if (result != 1) {
                throw new InvalidCommentException(ErrorType.COMMENT_NOT_CREATED);
            }

            UserSecurityDto postWriterSecurityDto = postDao.selectUserDtoByPno(commentDto.getPno()).toUserSecurityDto();
            notificationService.notifyNewComment(postWriterSecurityDto, commentDto);
            log.info("---- userSecurityDto = {}", postWriterSecurityDto);
        } catch (DataAccessException e) {
            log.error("Database error occurred while creating comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_CREATED);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while creating comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_CREATED);
        }
    }

    @Override
    public void updateComment(CommentDto commentDto) throws InvalidCommentException {
        try {
            checkAdminOrWriter(commentDto.getCno());
            int result = commentDao.updateComment(commentDto);

            if (result != 1) {
                throw new InvalidCommentException(ErrorType.COMMENT_NOT_UPDATED);
            }
        } catch (DataAccessException e) {
            log.error("Database error occurred while updating comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_UPDATED);
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException occurred while deleting comment", e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while updating comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_UPDATED);
        }
    }

    @Override
    public void deleteComment(Map map) {
        try {
            checkAdminOrWriter((Integer) map.get("cno"));

            int result = commentDao.deleteComment(map);
            if (result != 1) {
                throw new InvalidCommentException(ErrorType.COMMENT_NOT_DELETED);
            }
        } catch (DataAccessException e) {
            log.error("Database error occurred while deleting comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_DELETED);
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException occurred while deleting comment", e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while deleting comment", e);
            throw new InvalidCommentException(ErrorType.COMMENT_NOT_DELETED);
        }
    }

    @Override
    public int countCommentsByUno(SearchCondition sc) {
        return commentDao.countCommentsByUno(sc);
    }

    @Override
    public List<CommentDto> getCommentPageByUno(SearchCondition sc) {
        return commentDao.getCommentPageByUno(sc);
    }

    private void checkAdminOrWriter(Integer cno) throws UnauthorizedException {
        sessionUtils.checkAdminOrWriter(selectCommentByCno(cno));
    }


}
