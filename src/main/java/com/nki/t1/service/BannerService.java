package com.nki.t1.service;

import com.nki.t1.component.FileUploader;
import com.nki.t1.dao.AnnounceDao;
import com.nki.t1.dao.BannerDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.BannerDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.exception.InvalidAnnounceException;
import com.nki.t1.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Slf4j
@Service
public class BannerService {

    private final BannerDao bannerDao;
    private final AnnounceDao announceDao;
    private final FileUploader fileUploader;

    public BannerService(BannerDao bannerDao,
                         AnnounceDao announceDao,
                         @Qualifier("LocalBaseFileUploaderBanner")
                         FileUploader fileUploader) {
        this.bannerDao = bannerDao;
        this.announceDao = announceDao;
        this.fileUploader = fileUploader;
    }

    public List<BannerDto> selectBannerList() {
        // 이미지 가져오기
        return bannerDao.selectBannerList();
    }

    public BannerDto selectBannerById(int id) {
        // 이미지 가져오기
        return bannerDao.selectBannerById(id);
    }

    public List<BannerDto> selectActiveBanners() {
        // 이미지 가져오기
        return bannerDao.selectActiveBanners();
    }

    // 배너생성
    public void createBanner(BannerDto bannerDto) throws IOException {
        comparePostAt(bannerDto);
        // 파일 업로드
        bannerDto.setFileDto(fileUploader.upload(bannerDto.getImage()));
        // 파일 업로드 db 저장
        bannerDao.insertBanner(bannerDto);
    }

    // 배너 수정 - 이미지 포함
    public void updateBannerWithImage(BannerDto bannerDto) throws IOException {
        comparePostAt(bannerDto);
        // 기존 파일 삭제
        FileDto fileDto = bannerDao.selectBannerById(bannerDto.getId()).getFileDto();
        if (fileDto == null) { throw new InvalidFileException(ErrorType.FILE_NOT_DELETED);}

        fileUploader.delete(fileDto, true);

        // 파일 업로드
        bannerDto.setFileDto(fileUploader.upload(bannerDto.getImage()));
        // 파일 업로드 db 저장
        bannerDao.updateBannerWithImage(bannerDto);
    }

    // 배너 수정 - 이미지 제외
    public void updateBannerWithOutImage(BannerDto bannerDto) throws IOException {
        comparePostAt(bannerDto);

        // db 저장
        bannerDao.updateBannerWithOutImage(bannerDto);
    }

    public void deleteBanner(int bannerId) throws IOException {

        FileDto fileDto = bannerDao.selectBannerById(bannerId).getFileDto();
        if (fileDto == null) { throw new InvalidFileException(ErrorType.FILE_NOT_DELETED);}

        fileUploader.delete(fileDto, true);

        bannerDao.deleteBanner(bannerId);
    }

    // 배너게시일이 공지게시일 이전이면, 예외가 발생한다.
    private void comparePostAt(BannerDto bannerDto) {
        Date bannerDt = bannerDto.getPostedAt();
        Date announceDt = announceDao.selectAnnounceAdmin(bannerDto.getAnnounceDto().getAno()).getPostedAt();

        if (bannerDt.before(announceDt)) {
            throw new InvalidAnnounceException(ErrorType.ANNOUNCE_INVALID_POSTED_AT);
        }
    }

    // 홈 배너
    public List<BannerDto> getBannerList() {
        List<BannerDto> list = bannerDao.selectActiveBanners();
        // 기간내 등록된 배너가 없으면 기본 배너를 노출한다.
        if (list.isEmpty() && bannerDao.isDefaultBannerExist()) {
            list.add(bannerDao.selectDefaultBanner());
        }
        return list;
    }


    // 기본 배너
    public BannerDto getDefaultBanner() {
        if (bannerDao.isDefaultBannerExist()) {
            return bannerDao.selectDefaultBanner();
        }
        return null;
    }

    @Transactional
    public void updateBannerDefault(BannerDto bannerDto) throws IOException {
        // 파일 업로드
        bannerDto.setFileDto(fileUploader.upload(bannerDto.getImage()));

        // 파일 업로드 db 저장
        if (!bannerDao.isDefaultBannerExist()) {
            bannerDao.insertDefaultBanner(bannerDto);
        } else {
            // 기존 배너 삭제
            BannerDto previousBannerDto = bannerDao.selectDefaultBanner();
            fileUploader.delete(previousBannerDto.getFileDto(), true);

            bannerDao.updateDefaultBanner(bannerDto);
        }

    }


}
