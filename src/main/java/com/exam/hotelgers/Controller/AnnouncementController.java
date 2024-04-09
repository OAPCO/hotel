package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.service.AnnouncementService;
import com.exam.hotelgers.service.SelectMenuService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AnnouncementController {
    private final AnnouncementService announcementService;



    @GetMapping("/announcement/register")
    public String register() {
        return "announcement/register";
    }


    @PostMapping("/announcement/register")
    public String registerProc(@Valid AnnouncementDTO announcementDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("announcement registerProc 도착 " + announcementDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(announcementDTO);

        Long noticeIdx = announcementService.register(announcementDTO);

        redirectAttributes.addFlashAttribute("result", noticeIdx);

        return "redirect:/announcement/list";
    }


    @GetMapping("/announcement/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("announcement listForm 도착 ");

        Page<AnnouncementDTO> announcementDTOS = announcementService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(announcementDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", announcementDTOS);
        return "announcement/list";
    }




    @GetMapping("/announcement/modify/{noticeIdx}")
    public String modifyForm(@PathVariable Long noticeIdx, Model model) {

        log.info("announcement modifyProc 도착 " + noticeIdx);

        AnnouncementDTO announcementDTO = announcementService.read(noticeIdx);

        log.info("수정 전 정보" + announcementDTO);
        model.addAttribute("announcementDTO", announcementDTO);
        return "announcement/modify";
    }


    @PostMapping("/announcement/modify")
    public String modifyProc(@Validated AnnouncementDTO announcementDTO,
                             BindingResult bindingResult, Model model) {

        log.info("announcement modifyProc 도착 " + announcementDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/announcement/modify";
        }


        announcementService.modify(announcementDTO);

        log.info("업데이트 이후 정보 " + announcementDTO);

        return "redirect:/announcement/list";
    }

    @GetMapping("/announcement/delete/{noticeIdx}")
    public String deleteProc(@PathVariable Long noticeIdx) {

        announcementService.delete(noticeIdx);

        return "redirect:/announcement/list";
    }

    @GetMapping("/announcement/{noticeIdx}")
    public String readForm(@PathVariable Long noticeIdx, Model model) {
        AnnouncementDTO announcementDTO=announcementService.read(noticeIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("announcementDTO",announcementDTO);
        return "announcement/read";
    }
}
