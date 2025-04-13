package com.nki.t1.controller;

import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.AnnounceDto;
import com.nki.t1.dto.FileDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.exception.InvalidFileException;
import com.nki.t1.service.AnnounceService;
import com.nki.t1.service.FileAnnounceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// 사용자 페이지 - 공지사항
@Slf4j
@Controller
@RequestMapping("/user/announces")
public class UserAnnounceController {

    private final AnnounceService announceService;
    private final FileAnnounceService fileAnnounceService;
    private final MessageSource messageSource;

    private final String PAGE_PATH = "pagePath";
    private final List<Url> USER_ANNOUNCE_PATH = Url.getPath(Url.USER_ANNOUNCE);

    public UserAnnounceController(AnnounceService announceService,
                                  FileAnnounceService fileAnnounceService,
                                  MessageSource messageSource) {
        this.announceService = announceService;
        this.fileAnnounceService = fileAnnounceService;;
        this.messageSource = messageSource;

    }

    // 공지 페이지 진입
    @GetMapping("")
    public String userAnnounce(@ModelAttribute SearchCondition sc, Model model) {
        log.info("user sc={}", sc.getQueryString());
        model.addAttribute("sc", sc);
        model.addAttribute(PAGE_PATH, USER_ANNOUNCE_PATH);

        return "user.announce.announce";
    }

    // 공지 리스트
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> list(@ModelAttribute @Valid SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Object> map = new HashMap<>();
        int totalCnt = announceService.countAnnounceUser(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<AnnounceDto> list = announceService.getAnnouncePageUser(sc);

        map.put("list", list);
        map.put("ph", pageHandler);
        map.put("sc", sc);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{ano}")
    public String read(@PathVariable Integer ano, SearchCondition sc, Model model) {
        AnnounceDto announceDto = announceService.selectAnnounceUser(ano);
        model.addAttribute("announceDto", announceDto);
        model.addAttribute("sc", sc);


        return "user.announce.announceRead";
    }

    // 파일 다운로드
    @GetMapping("/files/{fano}")
    public ResponseEntity<Resource> download(@PathVariable Integer fano) throws MalformedURLException {
        FileDto fileDto = fileAnnounceService.selectFileByFnoUser(fano);

        if (fano == null || fano <= 0) {
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND);
        }

        UrlResource resource = new UrlResource("file:" + fileDto.getUploadFullPath());
        String encodedOrgName = UriUtils.encode(fileDto.getFileOrgNm(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOrgName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
