package com.nki.t1.dao;

import com.nki.t1.dto.BannerDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BannerDaoImpl implements BannerDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.BannerMapper.";

    @Override
    public List<BannerDto> selectBannerList() {
        return sqlSession.selectList(namespace + "selectBannerList");
    }

    @Override
    public BannerDto selectBannerById(int id) {
        return sqlSession.selectOne(namespace + "selectBannerById", id);
    }

    @Override
    public List<BannerDto> selectActiveBanners() {
        return sqlSession.selectList(namespace + "selectActiveBanners");
    }

    @Override
    public int insertBanner(BannerDto bannerDto) {
        return sqlSession.insert(namespace + "insertBanner", bannerDto);
    }

    @Override
    public int updateBannerWithImage(BannerDto bannerDto) {
        return sqlSession.update(namespace + "updateBannerWithImage", bannerDto);
    }

    @Override
    public int updateBannerWithOutImage(BannerDto bannerDto) {
        return sqlSession.update(namespace + "updateBannerWithOutImage", bannerDto);
    }

    @Override
    public int deleteBanner(int id) {
        return sqlSession.delete(namespace + "deleteBanner", id);
    }

    @Override
    public Boolean isDefaultBannerExist() {
        return sqlSession.selectOne(namespace + "isDefaultBannerExist");
    }

    @Override
    public BannerDto selectDefaultBanner() {
        return sqlSession.selectOne(namespace + "selectDefaultBanner");
    }

    @Override
    public void insertDefaultBanner(BannerDto bannerDto) {
        sqlSession.insert(namespace + "insertDefaultBanner", bannerDto);
    }

    @Override
    public void updateDefaultBanner(BannerDto bannerDto) {
        sqlSession.update(namespace + "updateDefaultBanner", bannerDto);
    }
}
