package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.service.MenuOptionService;
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
public class MenuoptionController {

    private final MenuOptionService menuOptionService;



    @GetMapping("/menuoption/register")
    public String register() {
        return "menuoption/register";
    }


    @PostMapping("/menuoption/register")
    public String registerProc(@Valid MenuOptionDTO menuoptionDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("menuoption registerProc 도착 " + menuoptionDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(menuoptionDTO);

        Long menuOptionIdx = menuOptionService.register(menuoptionDTO);

        redirectAttributes.addFlashAttribute("result", menuOptionIdx);

        return "redirect:/menuoption/list";
    }


    @GetMapping("/menuoption/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("menuoption listForm 도착 ");

        Page<MenuOptionDTO> menuOptionDTOS = menuOptionService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(menuOptionDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", menuOptionDTOS);
        return "menuoption/list";
    }




    @GetMapping("/menuoption/modify/{menuOptionIdx}")
    public String modifyForm(@PathVariable Long menuOptionIdx, Model model) {

        log.info("menuoption modifyProc 도착 " +  menuOptionIdx);

        MenuOptionDTO menuoptionDTO = menuOptionService.read(menuOptionIdx);

        log.info("수정 전 정보" + menuoptionDTO);
        model.addAttribute("menuoptionDTO", menuoptionDTO);
        return "menuoption/modify";
    }


    @PostMapping("/menuoption/modify")
    public String modifyProc(@Validated MenuOptionDTO menuoptionDTO,
                             BindingResult bindingResult, Model model) {

        log.info("menuoption modifyProc 도착 " + menuoptionDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/menuoption/modify";
        }


        menuOptionService.modify(menuoptionDTO);

        log.info("업데이트 이후 정보 " + menuoptionDTO);

        return "redirect:/menuoption/list";
    }

    @GetMapping("/menuoption/delete/{menuOptionIdx}")
    public String deleteProc(@PathVariable Long menuOptionIdx) {

        menuOptionService.delete(menuOptionIdx);

        return "redirect:/menuoption/list";
    }

    @GetMapping("/menuoption/{menuOptionIdx}")
    public String readForm(@PathVariable Long menuOptionIdx, Model model) {
        MenuOptionDTO menuoptionDTO=menuOptionService.read(menuOptionIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("menuoptionDTO",menuoptionDTO);
        return "menuoption/read";
    }
}
