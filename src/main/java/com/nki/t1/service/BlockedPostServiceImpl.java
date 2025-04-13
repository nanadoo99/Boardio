package com.nki.t1.service;

import com.nki.t1.dao.BlockedPostDao;
import com.nki.t1.dto.BlockedPostDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlockedPostServiceImpl implements BlockedPostService {
    private BlockedPostDao blockedPostDao;

    public BlockedPostServiceImpl(BlockedPostDao blockedPostDao) {
        this.blockedPostDao = blockedPostDao;
    }

    @Transactional
    @Override
    public void blockPost (BlockedPostDto blockedPostDto) throws RuntimeException{
        try {
            // 기존 사유 삭제
            blockedPostDao.deleteBlockedPost(blockedPostDto);
            // 새로운 사유 등록
            for(Integer brno : blockedPostDto.getBrnoList()) {
                blockedPostDto.setBrno(brno);
                if(blockedPostDao.blockPost(blockedPostDto) != 1) throw new RuntimeException("blocked post failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("blocked post failed");
        }
    }

    @Override
    public List<BlockedPostDto> selectBlocksByPno(int pno) {
        return blockedPostDao.selectBlocksByPno(pno);
    }

    @Override
    public int deleteBlockedPost(BlockedPostDto blockedPostDto) {
        return blockedPostDao.deleteBlockedPost(blockedPostDto);
    }


}
