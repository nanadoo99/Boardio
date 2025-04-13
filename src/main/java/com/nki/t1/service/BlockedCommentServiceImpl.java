package com.nki.t1.service;

import com.nki.t1.dao.BlockedCommentDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.BlockedCommentDto;
import com.nki.t1.exception.BadRequestException;
import com.nki.t1.exception.InvalidBlockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlockedCommentServiceImpl implements BlockedCommentService {
    private BlockedCommentDao blockedCommentDao;

    public BlockedCommentServiceImpl(BlockedCommentDao blockedCommentDao) {
        this.blockedCommentDao = blockedCommentDao;
    }

    @Transactional
    @Override
    public void blockComment (BlockedCommentDto blockedCommentDto) throws InvalidBlockException {
        try {
            // 기존 사유 삭제
            blockedCommentDao.deleteBlockedComment(blockedCommentDto);
            // 새로운 사유 등록
            for(Integer brno : blockedCommentDto.getBrnoList()) {
                blockedCommentDto.setBrno(brno);
                if(blockedCommentDao.blockComment(blockedCommentDto) != 1) throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new InvalidBlockException(ErrorType.REQUEST_FAILED);
        }
    }

    @Override
    public List<BlockedCommentDto> selectBlocksByPno(int pno) {
        return blockedCommentDao.selectBlocksByPno(pno);
    }

    @Override
    public int deleteBlockedComment(BlockedCommentDto blockedCommentDto) {
        return blockedCommentDao.deleteBlockedComment(blockedCommentDto);
    }


}
