package com.nki.t1.controller;

import com.nki.t1.domain.PageHandler;
import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserDto;
import com.nki.t1.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/post")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("mode", "create");
        return "post";
    }

    @PostMapping("/updateMode")
    public String updateMode(PostDto postDto, SearchCondition sc, Model model) {
        model.addAttribute("mode", "update");
        model.addAttribute("postDto", postDto);
        model.addAttribute("sc", sc);
        return "post";
    }

    @PostMapping("/create")
    public String writePost(PostDto postDto, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        HttpSession session = request.getSession();
        try {
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            postDto.setUno(userDto.getUno());

            if (postService.createPost(postDto) != 1)
                throw new Exception("Write Failed");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "CREATE");
            redirectAttributes.addFlashAttribute("postDto", postDto);
            return "redirect:/user/post/create";
        }
        return "redirect:/user/post/read?pno=" + postDto.getPno();
    }

    @PostMapping("/update")
    public String updatePost(PostDto postDto, SearchCondition sc, Model model, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            postDto.setUno(userDto.getUno());

            if(postService.updatePost(postDto)!=1)
                throw new Exception("update failed");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("failed", "UPDATE");
            model.addAttribute("mode", "update");
            model.addAttribute("postDto", postDto);
            model.addAttribute("sc", sc);
            return "post";
        }
        return "redirect:/user/post/read" + sc.getQueryString() + "&pno=" + postDto.getPno();

    }

    @PostMapping("/delete")
    public String deletePost(Integer pno, SearchCondition sc, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();

        try {
            UserDto userDto = (UserDto) session.getAttribute("authUser");
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("pno", pno);
            map.put("uno", userDto.getUno());

            if(postService.deletePost(map) != 1)
                throw new Exception("Delete Failed");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "DELETE");
            return "redirect:/user/post/read?pno=" + pno;
        }
        redirectAttributes.addFlashAttribute("sucess", "DELETE");
        return "redirect:/user/post/list" + sc.getQueryString();
    }

    @GetMapping("/read")
    public String readGet(Integer pno, SearchCondition sc, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostDto postDto = postService.getPost(pno);
            model.addAttribute("mode", "read");
            model.addAttribute("postDto", postDto);
            model.addAttribute("sc", sc);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "READ");
            return "redirect:/user/post/list"+sc.getQueryString();
        }
        return "post";
    }

    @GetMapping("/list")
    public String listGet(SearchCondition sc, Model model) {

        int totalCnt = postService.getPostCount(sc);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<PostDto> list = postService.getPage(sc);
        model.addAttribute("list", list);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("ph", pageHandler);

        Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        model.addAttribute("startOfToday", startOfToday.toEpochMilli());

        return "list";
    }
}
