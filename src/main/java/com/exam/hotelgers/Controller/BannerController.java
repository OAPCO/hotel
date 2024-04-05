package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.service.BannerService;
import com.exam.hotelgers.service.MemberService;
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
public class BannerController {


    private final BannerService bannerService;



    @GetMapping("/banner/register")
    public String register() {
        return "banner/register";
    }


    @PostMapping("/banner/register")
    public String registerProc(@Valid BannerDTO bannerDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("banner registerProc 도착" + bannerDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error 발생");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(bannerDTO);

        Long bannerIdx = bannerService.register(bannerDTO);

        redirectAttributes.addFlashAttribute("result", bannerIdx);

        return "redirect:/banner/list";
    }


    @GetMapping("/banner/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("Banner listForm 도착 ");

        Page<BannerDTO> bannerDTOS = bannerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(bannerDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", bannerDTOS);
        return "banner/list";
    }




    @GetMapping("/banner/modify/{bannerIdx}")
    public String modifyForm(@PathVariable Long bannerIdx, Model model) {

        log.info("Banner modifyForm 도착 " + bannerIdx);

        BannerDTO bannerDTO = bannerService.read(bannerIdx);

        log.info("수정 전 정보" + bannerDTO);
        model.addAttribute("bannerDTO", bannerDTO);
        return "banner/modify";
    }


    @PostMapping("/banner/modify")
    public String modifyProc(@Validated BannerDTO bannerDTO,
                             BindingResult bindingResult, Model model) {


        log.info("Banner modifyProc 도착 " + bannerDTO);



        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/banner/modify";
        }
        bannerService.modify(bannerDTO);

        log.info("업데이트 이후 정보 " + bannerDTO);

        return "redirect:/banner/list";
    }

    @GetMapping("/banner/delete/{bannerIdx}")
    public String deleteProc(@PathVariable Long bannerIdx) {
        bannerService.delete(bannerIdx);
        //서비스처리(삭제)
        return "redirect:/banner/list";
    }
}
