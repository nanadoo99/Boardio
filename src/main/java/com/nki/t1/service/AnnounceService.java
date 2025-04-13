package com.nki.t1.service;

import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.dto.SearchCondition;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AnnounceService {
   int countAnnounceAdmin(SearchCondition sc);
   List<AnnounceDto> getAnnouncePageAdmin(SearchCondition sc);
   AnnounceDto selectAnnounceAdmin(int ano);
   boolean isAnnouncePosted(int ano);

   void createAnnounce(AnnounceDto announceDto) throws IOException;
   void updateAnnounce(AnnounceDto announceDto) throws IOException;
   void deleteAnnounce(AnnounceDto announceDto);
   FileDto ckFileUpload(MultipartFile file) throws IOException;

   int countAnnounceUser(SearchCondition sc);
   List<AnnounceDto> getAnnouncePageUser(SearchCondition sc);
   AnnounceDto selectAnnounceUser(int ano);

    List<AnnounceDto> selectAnnounceListByPostedAt(SearchCondition sc);
    List<AnnounceDto> selectAnnounceScheduleListByPostedAt();

    void cleanUnusedImages() throws IOException;
}
