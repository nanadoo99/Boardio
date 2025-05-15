package com.nki.t1.controller;

import com.nki.t1.component.PageHandler;
import com.nki.t1.domain.ErrorDetail;
import com.nki.t1.domain.Url;
import com.nki.t1.dto.*;
import com.nki.t1.service.BlockReasonService;
import com.nki.t1.service.PostService;
import com.nki.t1.service.ReportedPostsService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// 사용자 페이지 - 게시글
@Slf4j
@Controller
@RequestMapping("/user/post")
public class UserPostController {

    private final PostService postService;
    private final ReportedPostsService reportedPostsService;
    private final BlockReasonService blockReasonService;

    private final SessionUtils sessionUtils;
    private final String mypage = "mypost";

    private final String PAGE_PATH = "pagePath";
    private final List<Url> USER_POST_PATH = Url.getPath(Url.USER_POST);
    private final MessageSource messageSource;

    public UserPostController(ReportedPostsService reportedPostsService,
                              BlockReasonService blockReasonService,
                              PostService postService,
                              SessionUtils sessionUtils,
                              MessageSource messageSource) {
        this.reportedPostsService = reportedPostsService;
        this.blockReasonService = blockReasonService;
        this.postService = postService;
        this.sessionUtils = sessionUtils;
        this.messageSource = messageSource;
    }


    // 게시글 리스트 - 화면 진입
    @GetMapping("")
    public String post(Model model) {
        model.addAttribute(PAGE_PATH, USER_POST_PATH);

        return "user.post.postList";
    }

    // 게시글 리스트 - 데이터 불러오기
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postList(@Valid SearchCondition sc, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ErrorDetail errorDetail = new ErrorDetail.builder(bindingResult.getFieldError(), messageSource, Locale.getDefault()).build();
            Map<String, Object> map = new HashMap<>();
            map.put("errorResponse", errorDetail);

            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> map = new HashMap<>();
        int totalCnt = postService.getPostCount(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);
        List<PostDto> list = postService.getPostPage(sc);

        map.put("list", list);
        map.put("ph", pageHandler);
        map.put("sc", sc);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // 특정 게시물 페이지 진입
    @GetMapping("/read/{pno}")
    public String read(@PathVariable Integer pno, SearchCondition sc, Model model) {
        List<BlockReasonsDto> brList = blockReasonService.selectBlockReasons();

        model.addAttribute("sc", sc);
        model.addAttribute("pno", pno);
        model.addAttribute("brList", brList);

        return "user.post.postRead";
    }

    // 특정 게시물 읽기
    @PostMapping("/read/{pno}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Integer pno) {
//        test();

        HashMap<String, Object> map = new HashMap<>();
        UserDto userDto = sessionUtils.getUserDto();
        ReportedPostsDto rp = new ReportedPostsDto();

        rp.setPno(pno);
        rp.setReportUno(userDto.getUno());

        PostDto postDto = postService.readPost(pno);
        ReportedPostsDto postReport = reportedPostsService.getReportByUnoPno(rp);

        map.put("postDto", postDto);
        map.put("postReport", postReport);

        return new ResponseEntity<>(map, HttpStatus.OK);

    }
/*
    private void test(){
        // 현재 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // principal 객체 가져오기
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // principal이 UserDetails일 경우 정보를 로그로 출력
            String username = ((UserDetails) principal).getUsername();
            log.info("Username: " + username);

            // 권한 목록 출력
            ((UserDetails) principal).getAuthorities().forEach(authority -> {
                log.info("Authority: " + authority.getAuthority());
            });
        } else {
            // UserDetails가 아닌 경우 principal 정보 출력
            log.info("Principal: " + principal.toString());
        }
    }*/

    // 게시글 작성 - 화면 진입
    @GetMapping("/postNew")
    public String postNew(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "user.post.postCreate";
    }

    // 게시글 작성 - 게시글 저장
    @PostMapping("/post")
    public String create(@Valid PostDto postDto, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes reAttr) {
        if(bindingResult.hasErrors()) {
            return "user.post.postCreate";
        }

        UserDto userDto = sessionUtils.getUserDto();
        postDto.setUno(userDto.getUno());

        postService.savePost(postDto, request);
        reAttr.addFlashAttribute("mode", "create");

        return "redirect:/user/post/read/" + postDto.getPno();

    }

    // 게시글 작성 - 이미지 업로드 (ckEditor)
    @PostMapping("/ckUpload")
    @ResponseBody
    public Map<String, Object> ckUpload(@RequestParam("upload") MultipartFile ckFile) {
        Map<String, Object> result = new HashMap<>();

        try {
            FileDto ckFileDto = postService.ckFileUpload(ckFile);

            result.put("uploaded", true);
            result.put("fileName", ckFileDto.getFileOrgNm());
            result.put("url", ckFileDto.getUploadPath());

            return result;
        } catch (IOException e) {
            result.put("uploaded", false);

            return result;
        }
    }

    // 게시글 수정 - 화면 진입
    @GetMapping("/postEdit/{pno}")
    public String postEdit(@PathVariable("pno") Integer pno, @RequestParam(required = false, defaultValue = "") String orgPage, HttpServletRequest request, @RequestParam(value = "error", required = false) boolean error, Model model) {
        PostDto postDto = postService.getPostWithAuth(pno);

        // postDto가 null이 아닐 경우에만 진행
        if (postDto != null) {
            if (!error) {
                model.addAttribute(postDto);
            }
            setModelForPostUpdatePage(model, request, orgPage);
        }

        return "user.post.postCreate";

    }

    // 게시글 수정
    @PostMapping("/postEdit")
    public String upload(@Valid PostDto postDto, BindingResult bindingResult, String orgPage, HttpServletRequest request, RedirectAttributes reAttr, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(postDto);
            setModelForPostUpdatePage(model, request, orgPage);
            return "user.post.postCreate";
        }

        postService.updatePost(postDto, request);
        reAttr.addFlashAttribute("mode", "update");
        if (isFromMyPage(request, orgPage)) {
            return "redirect:/user/mypost/read/" + postDto.getPno();
        }
        return "redirect:/user/post/read/" + postDto.getPno(); //return UriComponentsBuilder.newInstance() 을 이용하여, 타일즈를 통한 리다이렉트에서도 쿼리 문자열이 유지된다.
    }

    // 게시글 삭제
    @GetMapping("/postDelete/{pno}")
    public String delete(@PathVariable("pno") Integer pno, @RequestParam(required = false, defaultValue = "") String orgPage, SearchCondition sc, HttpServletRequest request, RedirectAttributes reAttr) {
        postService.deletePost(pno, request);
        reAttr.addFlashAttribute("mode", "deleteFileSetFromServer");
        if (isFromMyPage(request, orgPage)) {
            return "redirect:/user/mypost/list";
        }
        return "redirect:/user/post?page=1";
    }

    private boolean isFromMyPage(HttpServletRequest request, String orgPage) {
        return request.getHeader("Referer").contains(mypage) || orgPage.equals(mypage);
    }

    // 게시글 신고
    @PostMapping("/report")
    @ResponseBody
    public ResponseEntity<String> report(ReportedPostsDto reportedPostsDto) {
        reportedPostsService.reportByUnoPno(reportedPostsDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시글 신고 취소
    @PostMapping("/deleteReport/{pno}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteReport(@PathVariable Integer pno) {
        HashMap<String, Object> map = new HashMap<>();
        UserDto userDto = sessionUtils.getUserDto();

        ReportedPostsDto rp = new ReportedPostsDto(pno, userDto.getUno(), userDto.getUno());
        reportedPostsService.deleteReportByUnoPno(rp);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // create 와 update 모드를 구분하고, 게시글 진입 경로(마이페이지 혹은 일반 게시판)를 구분한다.
    private void setModelForPostUpdatePage(Model model, HttpServletRequest request, String orgPage) {
        model.addAttribute("mode", "update");
        if (isFromMyPage(request, orgPage)) {
            model.addAttribute("orgPage", mypage);
        }
    }
}
