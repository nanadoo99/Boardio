package com.nki.t1.dao;

import com.nki.t1.dto.BannerDto;

import java.util.List;

public interface BannerDao {

    List<BannerDto> selectBannerList();
    BannerDto selectBannerById(int id);
    List<BannerDto> selectActiveBanners();

    int insertBanner(BannerDto bannerDto);
    int updateBannerWithImage(BannerDto bannerDto);
    int updateBannerWithOutImage(BannerDto bannerDto);
    int deleteBanner(int id);

    Boolean isDefaultBannerExist();
    BannerDto selectDefaultBanner();
    void insertDefaultBanner(BannerDto bannerDto);
    void updateDefaultBanner(BannerDto bannerDto);
}
