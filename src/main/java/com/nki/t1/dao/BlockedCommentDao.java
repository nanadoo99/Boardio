package com.nki.t1.dao;

import com.nki.t1.dto.BlockedCommentDto;

import java.util.List;

public interface BlockedCommentDao {
    int blockComment(BlockedCommentDto blockedCommentDto);
    int deleteBlockedComment(BlockedCommentDto blockedCommentDto);
    List<BlockedCommentDto> selectBlocksByPno(int cno);
}
