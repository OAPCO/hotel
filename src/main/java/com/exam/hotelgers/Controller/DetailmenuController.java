package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.DetailmenuDTO;
import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.service.DetailmenuService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class DetailmenuController {

    private final DetailmenuService detailmenuService;
    private final HttpServletRequest request;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;

    @GetMapping("/detailmenu/register")
    public String register() {
        return "detailmenu/register";
    }


    @PostMapping("/detailmenu/register")
    public String registerProc(@Valid DetailmenuDTO detailmenuDTO,
                               BindingResult bindingResult,
                               @RequestParam(required = false) MultipartFile imgFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        log.info("detailmenu registerProc 도착 " + detailmenuDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(detailmenuDTO);

        Long detailmenuIdx = detailmenuService.register(detailmenuDTO, imgFile);

        redirectAttributes.addFlashAttribute("result", detailmenuIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }


    @GetMapping("/detailmenu/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("detailmenu listForm 도착 ");

        Page<DetailmenuDTO> detailmenuDTOS = detailmenuService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(detailmenuDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", detailmenuDTOS);
        return "detailmenu/list";
    }




    @GetMapping("/detailmenu/modify/{detailmenuIdx}")
    public String modifyForm(@PathVariable Long detailmenuIdx, Model model) {

        log.info("detailmenu modifyProc 도착 " + detailmenuIdx);

        DetailmenuDTO detailmenuDTO = detailmenuService.read(detailmenuIdx);

        log.info("수정 전 정보" + detailmenuDTO);
        model.addAttribute("detailmenuDTO", detailmenuDTO);
        return "detailmenu/modify";
    }


    @PostMapping("/detailmenu/modify")
    public String modifyProc(@Validated DetailmenuDTO detailmenuDTO,
                             @RequestParam(required = false)MultipartFile imgFile,
                             BindingResult bindingResult, Model model) throws IOException {

        log.info("detailmenu modifyProc 도착 " + detailmenuDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생"+detailmenuDTO);

            return "/detailmenu/modify";
        }


        detailmenuService.modify(detailmenuDTO, imgFile);

        log.info("업데이트 이후 정보 " + detailmenuDTO);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }

    @GetMapping("/detailmenu/delete/{detailmenuIdx}")
    public String deleteProc(@PathVariable Long detailmenuIdx) throws IOException {

        detailmenuService.delete(detailmenuIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }

    @GetMapping("/detailmenu/{detailmenuIdx}")
    public String readForm(@PathVariable Long detailmenuIdx, Model model) {
        DetailmenuDTO detailmenuDTO=detailmenuService.read(detailmenuIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("detailmenuDTO",detailmenuDTO);
        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }
}
