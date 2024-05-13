package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.BannerService;
import com.exam.hotelgers.service.ImageService;
import com.exam.hotelgers.service.NoticeService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class NoticeController {


    private final NoticeService noticeService;
    private final HttpServletRequest request;



    @GetMapping("/admin/admin/notice/register")
    public String noticeRegister() {
        return "admin/admin/notice/register";
    }


    @PostMapping("/admin/admin/notice/register")
    public String noticeRegisterProc(@Valid NoticeDTO noticeDTO) {


        noticeService.register(noticeDTO);


        return "redirect:/admin/admin/notice/list";
    }



    @GetMapping("/admin/admin/notice/askregister")
    public String askRegister() {
        return "admin/admin/notice/askregister";
    }


    @PostMapping("/admin/admin/notice/askregister")
    public String askRegisterProc(@Valid NoticeDTO noticeDTO) {


        noticeService.register(noticeDTO);


        return "redirect:/admin/admin/notice/qnalist";
    }





    @GetMapping("/admin/admin/notice/list")
    public String noticeListForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        Page<NoticeDTO> noticeDTOS = noticeService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(noticeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", noticeDTOS);
        return "admin/admin/notice/list";
    }

    @PostMapping("/admin/admin/notice/list")
    public String qnaListProc(@PageableDefault(page = 1)Pageable pageable, Model model, SearchDTO searchDTO) throws Exception{


        Page<NoticeDTO> noticeDTOS = noticeService.getNoticeList(pageable,searchDTO);

        Map<String, Integer> pageinfo = PageConvert.Pagination(noticeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", noticeDTOS);

        return "admin/admin/notice/list";
    }



    @GetMapping("/admin/admin/notice/delete/{noticeIdx}")
    public String deleteProc(@PathVariable Long noticeIdx) {


        noticeService.delete(noticeIdx);
        return "redirect:/admin/admin/banner/list";
    }





}
