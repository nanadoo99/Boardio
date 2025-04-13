package com.nki.t1.service;

import com.nki.t1.dto.BlockedCommentDto;
import com.nki.t1.dto.BlockedCommentDto;

import java.util.List;

public interface BlockedCommentService {
    void blockComment(BlockedCommentDto blockedCommentDto);
    List<BlockedCommentDto> selectBlocksByPno (int cno);
    int deleteBlockedComment(BlockedCommentDto blockedCommentDto);
}
