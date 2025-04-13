package com.nki.t1.service;

import com.nki.t1.dao.BlockReasonDao;
import com.nki.t1.dto.BlockReasonsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockReasonServiceImpl implements BlockReasonService {

    private BlockReasonDao blockReasonDao;

    @Autowired
    public BlockReasonServiceImpl(BlockReasonDao blockReasonDao) {
        this.blockReasonDao = blockReasonDao;
    }


    public List<BlockReasonsDto> selectBlockReasons() {
        return blockReasonDao.selectBlockReasons();
    }



}
