package com.nki.t1.dao;

import com.nki.t1.dto.BlockReasonsDto;

import java.util.List;

public interface BlockReasonDao {
    List<BlockReasonsDto> selectBlockReasons();
}
