package com.nki.t1.dao;

import com.nki.t1.dto.BlockedPostDto;

import java.util.List;

public interface BlockedPostDao {
    int blockPost(BlockedPostDto blockedPostDto);
    int deleteBlockedPost(BlockedPostDto blockedPostDto);
    List<BlockedPostDto> selectBlocksByPno(int pno);
}
