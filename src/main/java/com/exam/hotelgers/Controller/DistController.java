package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.DistChiefDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.service.DistChiefService;
import com.exam.hotelgers.service.DistService;
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
public class DistController {
    
    private final DistService distService;
    private final DistChiefService distChiefService;



    @GetMapping("/dist/register")
    public String register() {
        return "dist/register";
    }


    @PostMapping("/dist/register")
    public String registerProc(@Valid DistDTO distDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("dist registerProc 도착 " + distDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(distDTO);

        Long distIdx = distService.register(distDTO);

        redirectAttributes.addFlashAttribute("result", distIdx);

        return "redirect:/dist/list";
    }



    @GetMapping("/distchief/register")
    public String register2() {
        return "distchief/register";
    }


    @PostMapping("/distchief/register")
    public String registerProc2(@Valid DistChiefDTO distChiefDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("dist registerProc 도착 " + distChiefDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(distChiefDTO);

        Long distChiefIdx = distChiefService.register(distChiefDTO);

        redirectAttributes.addFlashAttribute("result", distChiefIdx);

        return "redirect:/distchief/list";
    }





    @GetMapping("/dist/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("dist listForm 도착 ");

        Page<DistDTO> distDTOS = distService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distDTOS);
        return "dist/list";
    }

    @GetMapping("/distchief/list")
    public String listForm2(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("dist listForm 도착 ");

        Page<DistChiefDTO> distChiefDTOS = distChiefService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distChiefDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distChiefDTOS);
        return "distchief/list";
    }




    @GetMapping("/dist/modify/{distIdx}")
    public String modifyForm(@PathVariable Long distIdx, Model model) {

        log.info("dist modifyProc 도착 " + distIdx);

        DistDTO distDTO = distService.read(distIdx);

        log.info("수정 전 정보" + distDTO);
        model.addAttribute("distDTO", distDTO);
        return "dist/modify";
    }


    @PostMapping("/dist/modify")
    public String modifyProc(@Validated DistDTO distDTO,
                             BindingResult bindingResult, Model model) {

        log.info("dist modifyProc 도착 " + distDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/dist/modify";
        }


        distService.modify(distDTO);

        log.info("업데이트 이후 정보 " + distDTO);

        return "redirect:/dist/list";
    }

    @GetMapping("/dist/delete/{distIdx}")
    public String deleteProc(@PathVariable Long distIdx) {

        distService.delete(distIdx);

        return "redirect:/dist/list";
    }

    @GetMapping("/dist/{distIdx}")
    public String readForm(@PathVariable Long distIdx, Model model) {
        DistDTO distDTO=distService.read(distIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("distDTO",distDTO);
        return "dist/read";
    }
}
