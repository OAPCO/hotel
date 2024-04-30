package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.SelectMenuDTO;
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
public class SelectMenuController {
    private final SelectMenuService selectMenuService;



    @GetMapping("/selectmenu/register")
    public String register() {
        return "selectmenu/register";
    }


    @PostMapping("/selectmenu/register")
    public String registerProc(@Valid SelectMenuDTO selectmenuDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("selectmenu registerProc 도착 " + selectmenuDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(selectmenuDTO);

        Long selectMenuIdx = selectMenuService.register(selectmenuDTO);

        redirectAttributes.addFlashAttribute("result", selectMenuIdx);

        return "redirect:/selectmenu/list";
    }


    @GetMapping("/selectmenu/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("selectmenu listForm 도착 ");

        Page<SelectMenuDTO> selectmenuDTOS = selectMenuService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(selectmenuDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", selectmenuDTOS);
        return "selectmenu/list";
    }




    @GetMapping("/selectmenu/modify/{selectMenuIdx}")
    public String modifyForm(@PathVariable Long selectMenuIdx, Model model) {

        log.info("selectmenu modifyProc 도착 " + selectMenuIdx);

        SelectMenuDTO selectmenuDTO = selectMenuService.read(selectMenuIdx);

        log.info("수정 전 정보" + selectmenuDTO);
        model.addAttribute("selectmenuDTO", selectmenuDTO);
        return "selectmenu/modify";
    }


    @PostMapping("/selectmenu/modify")
    public String modifyProc(@Validated SelectMenuDTO selectmenuDTO,
                             BindingResult bindingResult, Model model) {

        log.info("selectmenu modifyProc 도착 " + selectmenuDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/selectmenu/modify";
        }


        selectMenuService.modify(selectmenuDTO);

        log.info("업데이트 이후 정보 " + selectmenuDTO);

        return "redirect:/selectmenu/list";
    }

    @GetMapping("/selectmenu/delete/{selectMenuIdx}")
    public String deleteProc(@PathVariable Long selectMenuIdx) {

        selectMenuService.delete(selectMenuIdx);

        return "redirect:/selectmenu/list";
    }

    @GetMapping("/selectmenu/{selectMenuIdx}")
    public String readForm(@PathVariable Long selectMenuIdx, Model model) {
        SelectMenuDTO selectmenuDTO=selectMenuService.read(selectMenuIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("selectmenuDTO",selectmenuDTO);
        return "selectmenu/read";
    }
}
