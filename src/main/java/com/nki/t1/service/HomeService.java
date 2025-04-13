package com.nki.t1.service;

import com.nki.t1.dao.AnnounceDao;
import com.nki.t1.dao.BannerDao;
import com.nki.t1.dao.PostDao;
import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.BannerDto;
import com.nki.t1.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HomeService {
    private final BannerDao bannerDao;
    private final AnnounceDao announceDao;

    public HomeService(BannerDao bannerDao, AnnounceDao announceDao) {
        this.bannerDao = bannerDao;
        this.announceDao = announceDao;
    }

    // 배너
    public List<BannerDto> getBannerList() {
        return bannerDao.selectActiveBanners();
    }

    // 공지사항
//    public List<AnnounceDto> getAnnounceList() {
//        return announceDao.getAnnouncePageUser();
//    }

}
