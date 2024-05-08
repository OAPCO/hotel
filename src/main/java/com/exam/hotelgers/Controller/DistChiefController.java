package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.DistChiefDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.service.DistChiefService;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.SearchService;
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

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class DistChiefController {

    private final DistChiefService distChiefService;


    @GetMapping("/admin/distchief/register")
    public String register2() {
        return "admin/distchief/register";
    }


    @PostMapping("/admin/distchief/register")
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

        return "redirect:/admin/distchief/list";
    }





    @GetMapping("/admin/distchief/list")
    public String listForm2(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("dist listForm 도착 ");

        Page<DistChiefDTO> distChiefDTOS = distChiefService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distChiefDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distChiefDTOS);
        return "admin/distchief/list";
    }






    @GetMapping("/admin/admin/distchief/register")
    public String distChiefRegisterForm() {


        return "admin/admin/distchief/register";

    }


    @PostMapping("/admin/admin/distchief/register")
    public String distChiefRegisterProc(@Valid DistChiefDTO distChiefDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {

        log.info("총판장생성 프록 " + distChiefDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        Long distChiefIdx = distChiefService.register(distChiefDTO);



        redirectAttributes.addFlashAttribute("result", distChiefIdx);

        return "admin/admin/distchief/list";
    }



    @GetMapping("/admin/admin/distchief/list")
    public String distchieflistForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("매장주 listchiefForm 도착 ");

        Page<DistChiefDTO> distChiefDTOS = distChiefService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distChiefDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distChiefDTOS);


        return "admin/admin/distchief/list";
    }
}
