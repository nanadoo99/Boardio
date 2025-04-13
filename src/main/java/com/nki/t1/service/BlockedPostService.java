package com.nki.t1.service;

import com.nki.t1.dto.BlockedPostDto;

import java.util.List;

public interface BlockedPostService {
    void blockPost(BlockedPostDto blockedPostDto);
    List<BlockedPostDto> selectBlocksByPno (int pno);
    int deleteBlockedPost(BlockedPostDto blockedPostDto);
}
