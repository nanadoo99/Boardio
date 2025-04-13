package com.nki.t1.dao;

import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.SearchCondition;

import java.util.List;

public interface AnnounceDao {

    int countAnnounceAdmin(SearchCondition sc);
    List<AnnounceDto> getAnnouncePageAdmin(SearchCondition sc);
    AnnounceDto selectAnnounceAdmin(int ano);

    int insertAnnounce(AnnounceDto announceDto);
    void updateAnnounce(AnnounceDto announceDto);
    int deleteAnnounce(AnnounceDto announceDto);

    int countAnnounceUser(SearchCondition sc);
    List<AnnounceDto> getAnnouncePageUser(SearchCondition sc);
    AnnounceDto selectAnnounceUser(int ano);

    List<AnnounceDto> selectAnnounceListByPostedAt(SearchCondition sc);
    List<AnnounceDto> selectAnnounceScheduleListByPostedAt();
}
