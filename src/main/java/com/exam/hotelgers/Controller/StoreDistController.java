package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.dto.StoreDistDTO;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.service.StoreDistService;
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
public class StoreDistController {
    
    private final StoreDistService storedistService;



    @GetMapping("/storedist/register")
    public String register() {
        return "storedist/register";
    }


    @PostMapping("/storedist/register")
    public String registerProc(@Valid StoreDistDTO storedistDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("storedist registerProc 도착 " + storedistDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(storedistDTO);

        Long storedistIdx = storedistService.register(storedistDTO);

        redirectAttributes.addFlashAttribute("result", storedistIdx);

        return "redirect:/storedist/list";
    }


    @GetMapping("/storedist/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("storedist listForm 도착 ");

        Page<StoreDistDTO> storedistDTOS = storedistService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(storedistDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", storedistDTOS);
        return "storedist/list";
    }




    @GetMapping("/storedist/modify/{storedistIdx}")
    public String modifyForm(@PathVariable Long storedistIdx, Model model) {

        log.info("storedist modifyProc 도착 " + storedistIdx);

        StoreDistDTO storedistDTO = storedistService.read(storedistIdx);

        log.info("수정 전 정보" + storedistDTO);
        model.addAttribute("storedistDTO", storedistDTO);
        return "storedist/modify";
    }


    @PostMapping("/storedist/modify")
    public String modifyProc(@Validated StoreDistDTO storedistDTO,
                             BindingResult bindingResult, Model model) {

        log.info("storedist modifyProc 도착 " + storedistDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/storedist/modify";
        }


        storedistService.modify(storedistDTO);

        log.info("업데이트 이후 정보 " + storedistDTO);

        return "redirect:/storedist/list";
    }

    @GetMapping("/storedist/delete/{storedistIdx}")
    public String deleteProc(@PathVariable Long storedistIdx) {

        storedistService.delete(storedistIdx);

        return "redirect:/storedist/list";
    }

    @GetMapping("/storedist/{storedistIdx}")
    public String readForm(@PathVariable Long storedistIdx, Model model) {
        StoreDistDTO storedistDTO=storedistService.read(storedistIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("storedistDTO",storedistDTO);
        return "storedist/read";
    }
}
