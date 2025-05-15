package com.nki.t1.controller;


import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import com.nki.t1.exception.InvalidFileException;
import com.nki.t1.service.AnnounceService;
import com.nki.t1.service.FileAnnounceService;
import com.nki.t1.utils.AwsS3Utils;
import com.nki.t1.utils.AwsS3UtilsAnnounceAttachment;
import com.nki.t1.utils.DateTimeFormatUtils;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// 관리자 페이지 - 공지 관리
@Slf4j
@Controller
@RequestMapping("/admin/announces")
public class AdminAnnounceController {

    private final AnnounceService announceService;
    private final AwsS3Utils awsS3Utils;
    private final FileAnnounceService fileAnnounceService;
    private final SessionUtils sessionUtils;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> ADMIN_ANNOUNCE_SCHEDULE_PATH = Url.getPath(Url.ADMIN_ANNOUNCE_SCHEDULE);
    private final List<Url> ADMIN_ANNOUNCE_LIST_PATH = Url.getPath(Url.ADMIN_ANNOUNCE_LIST);
    private final MessageSource messageSource;

    @Autowired
    public AdminAnnounceController(AnnounceService announceService,
                                   AwsS3UtilsAnnounceAttachment awsS3Utils,
                                   FileAnnounceService fileAnnounceService,
                                   SessionUtils sessionUtils,
                                   MessageSource messageSource) {
        this.announceService = announceService;
        this.awsS3Utils = awsS3Utils;
        this.fileAnnounceService = fileAnnounceService;
        this.sessionUtils = sessionUtils;
        this.messageSource = messageSource;
    }

    // 페이지 진입
    @GetMapping("")
    public String adminAnnounce(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_ANNOUNCE_LIST_PATH);

        return "admin.announce.adminAnnounce";
    }

    // 검색
    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> list(@Valid SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        sc.setUno(sessionUtils.getUserDto().getUno());
        HashMap<String, Object> map = new HashMap<>();
        int totalCnt = announceService.countAnnounceAdmin(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<AnnounceDto> list = announceService.getAnnouncePageAdmin(sc);

        map.put("list", list);
        map.put("ph", pageHandler);
        map.put("sc", sc);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 공지 등록 - 페이지 진입
    @GetMapping("/new")
    public String createPage(@RequestParam(required = false) String postedAt, Model model) {
        if(postedAt != null && postedAt.trim().length() > 0) {
            String newPostedAt = DateTimeFormatUtils.timestampStringToFormattedString(postedAt);
            model.addAttribute("postedAt", newPostedAt);
        }
        model.addAttribute("announceDto", new AnnounceDto());
        return "admin.announce.adminAnnounceCreate";
    }

    // 공지 생성
    @PostMapping("")
    public String create(@Valid @ModelAttribute("announceDto") AnnounceDto announceDto,
                         BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "admin.announce.adminAnnounceCreate";
        }
        UserDto userDto = SessionUtils.getUserDto();
        announceDto.setUno(userDto.getUno());
        announceService.createAnnounce(announceDto);

        return "redirect:/admin/announces/" + announceDto.getAno();

    }

    // 공지 읽기
    @GetMapping("/{ano}")
    public String read(@PathVariable("ano") int ano, SearchCondition sc, Model model) {
        AnnounceDto announceDto = announceService.selectAnnounceAdmin(ano);
        model.addAttribute("announceDto", announceDto);
        model.addAttribute("sc", sc);

        return "admin.announce.adminAnnounceRead";
    }

    // 파일 다운로드
    @GetMapping("/files/{fano}")
    public ResponseEntity<Resource> download(@PathVariable Integer fano) throws MalformedURLException {
        if (fano == null || fano <= 0) {
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND);
        }

        FileDto fileDto = fileAnnounceService.selectFileByFnoAdmin(fano);
        System.out.println("fileDto; = " + fileDto);

        InputStreamResource resource = new InputStreamResource(awsS3Utils.downloadObject(fileDto.getUploadPath()));
        String encodedOrgName = UriUtils.encode(fileDto.getFileOrgNm(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOrgName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // 게시글 삭제
    @DeleteMapping("/{ano}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("ano") Integer ano) {
        UserDto userDto = sessionUtils.getUserDto();
        sessionUtils.checkAdminAndWriter(announceService.selectAnnounceAdmin(ano));

        AnnounceDto announceDto = new AnnounceDto();
        announceDto.setUno(userDto.getUno());
        announceDto.setAno(ano);

        announceService.deleteAnnounce(announceDto);

        return ResponseEntity.ok("삭제 성공");
    }

    // 게시글 수정 - 화면 진입
    @GetMapping("/{ano}/form")
    public String edit(@PathVariable("ano") Integer ano, Model model) {
        AnnounceDto announceDto = announceService.selectAnnounceAdmin(ano);
        model.addAttribute("announceDto", announceDto);

        return "admin.announce.adminAnnounceCreate";
    }

    // 게시글 수정
    @PostMapping("/put")
    public String editPost(@Valid @ModelAttribute("announceDto") AnnounceDto announceDto,
                           BindingResult result,
                           @RequestParam(value = "fanoList", required = false) List<Integer> fanoList) throws IOException {
        if (result.hasErrors()) {
            return "admin.announce.adminAnnounceCreate";
        }
        // 권한체크
        sessionUtils.checkAdminAndWriter(announceService.selectAnnounceAdmin(announceDto.getAno()));

        // 선택 파일 삭제
        if(fanoList != null && !fanoList.isEmpty()) {
            for(Integer fano : fanoList) {
                // 파일 정보
                FileDto fileDto = fileAnnounceService.selectFileByFnoAdmin(fano);
                // 파일서버 삭제
                awsS3Utils.deleteObject(fileDto);
                // db 삭제
                fileAnnounceService.deleteFileListByFanoAdmin(fano);
            }
        }

        // 공지 업데이트
        announceService.updateAnnounce(announceDto);
        return "redirect:/admin/announces/" + announceDto.getAno();
    }

    // 사용자 페이지에 게시된 공지인지 확인
    @PostMapping("/{ano}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> isPosted(@PathVariable("ano") Integer ano) {
        boolean isPosted = announceService.isAnnouncePosted(ano); // 공지 유효성 확인

        Map<String, Boolean> response = new HashMap<>();
        response.put("isPosted", isPosted); // 검증 결과를 응답

        return ResponseEntity.ok(response);
    }

    // 게시글 작성 - 이미지 업로드
    @PostMapping("/images")
    @ResponseBody
    public Map<String, Object> ckUpload(@RequestParam("upload") MultipartFile ckFile) {
        Map<String, Object> result = new HashMap<>();

        try {
            FileDto ckFileDto = announceService.ckFileUpload(ckFile);

            result.put("uploaded", true);
            result.put("fileName", ckFileDto.getFileOrgNm());
            result.put("url", ckFileDto.getUploadPath());

            return result;
        } catch (IOException e) {
            result.put("uploaded", false);

            return result;
        }
    }

    // 공지 게시 일정표
    @GetMapping("/schedule")
    public String announceSchedule(@ModelAttribute SearchCondition sc, Model model) {
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, ADMIN_ANNOUNCE_SCHEDULE_PATH);

        return "admin.announce.adminAnnounceSchedule";
    }

    // 공지 게시 일정표 - 리스트
    @GetMapping("/schedule/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCalendarEvents() {
        HashMap<String, Object> map = new HashMap<>();
        List<AnnounceDto> list = announceService.selectAnnounceScheduleListByPostedAt();
        map.put("list", list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
