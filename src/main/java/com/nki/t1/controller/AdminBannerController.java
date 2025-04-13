package com.nki.t1.controller;


import com.nki.t1.domain.Url;
import com.nki.t1.dto.BannerDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.AnnounceService;
import com.nki.t1.service.BannerService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// 관리자 페이지 - 배너 관리
@Slf4j
@Controller
@RequestMapping("/admin/banners")
public class AdminBannerController {

    private final BannerService bannerService;
    private final AnnounceService announceService;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> ADMIN_BANNER_SCHEDULE_PATH = Url.getPath(Url.ADMIN_BANNER_SCHEDULE);
    private final List<Url> ADMIN_BANNER_DEFAULT_PATH = Url.getPath(Url.ADMIN_BANNER_DEFAULT);

    @Autowired
    public AdminBannerController(BannerService bannerService, AnnounceService  announceService) {
        this.bannerService = bannerService;
        this.announceService = announceService;
    }

    // 배너 관리 페이지
    @GetMapping("")
    public String adminBanner(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_BANNER_SCHEDULE_PATH);

        return "admin.banner.adminBanner";
    }

    // 베너 항목
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCalendarEvents() {
        Map<String, Object> map = new HashMap<>();
        List<BannerDto> list = bannerService.selectBannerList();
        map.put("list", list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 배너 조회
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBanner(@PathVariable("id") Integer id) {
        BannerDto bannerDto = bannerService.selectBannerById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("bannerDto", bannerDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너 생성
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createBanner(@ModelAttribute BannerDto bannerDto) throws IOException {
        UserDto userDto = SessionUtils.getUserDto();
        bannerDto.setUno(userDto.getUno());
        HashMap<String, Object> map = new HashMap<>();
        bannerService.createBanner(bannerDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너 수정
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBanner(@PathVariable Integer id, @ModelAttribute BannerDto bannerDto) throws IOException {
        UserDto userDto = SessionUtils.getUserDto();
        bannerDto.setUno(userDto.getUno());

        if(bannerDto.getImage() == null || bannerDto.getImage().isEmpty()) {
            bannerService.updateBannerWithOutImage(bannerDto);
        } else {
            bannerService.updateBannerWithImage(bannerDto);
        }

        HashMap<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteBanner(@PathVariable Integer id) throws IOException {
        bannerService.deleteBanner(id);
        HashMap<String, Object> map = new HashMap<>();

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너에 연결할 공지 목록
    @GetMapping("/announceList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAnnounceList(@ModelAttribute SearchCondition sc) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", announceService.selectAnnounceListByPostedAt(sc));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너에 연결할 공지 목록
    @GetMapping("/announceList2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAnnounceList2(@ModelAttribute SearchCondition sc) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", announceService.getAnnouncePageAdmin(sc));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 배너 이미지 다운로드
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> download(@PathVariable Integer id) throws MalformedURLException {
        FileDto fileDto = bannerService.selectBannerById(id).getFileDto();

        UrlResource resource = new UrlResource("file:" + fileDto.getUploadFullPath());
        String encodedOrgName = UriUtils.encode(fileDto.getFileOrgNm(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOrgName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    // 기본 배너 설정 페이지 진입
    @GetMapping("/default")
    public String defaultBanner(Model model) {
        model.addAttribute("bannerDto", bannerService.getDefaultBanner());
        model.addAttribute(PAGE_PATH, ADMIN_BANNER_DEFAULT_PATH);

        return "admin.banner.adminBannerDefault";
    }

    // 기본 배너 등록
    @PostMapping("/default")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateDefault(@ModelAttribute BannerDto bannerDto) throws  IOException {
        UserDto userDto = SessionUtils.getUserDto();
        bannerDto.setUno(userDto.getUno());

        bannerService.updateBannerDefault(bannerDto);

        HashMap<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

}
