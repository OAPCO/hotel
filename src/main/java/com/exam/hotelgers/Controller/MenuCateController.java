package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.MenuCateDTO;
import com.exam.hotelgers.service.MenuCateService;
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
public class MenuCateController {

    private final MenuCateService menucateService;



    @GetMapping("/menuCate/register")
    public String register() {
        return "menuCate/register";
    }


    @PostMapping("/menuCate/register")
    public String registerProc(@Valid MenuCateDTO menuCateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("menuCate registerProc 도착 " + menuCateDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(menuCateDTO);

        Long menuCateIdx = menucateService.register(menuCateDTO);

        redirectAttributes.addFlashAttribute("result", menuCateIdx);

        return "redirect:/admin/manager/order/menulist";
    }


    @GetMapping("/menuCate/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("menuCate listForm 도착 ");

        Page<MenuCateDTO> menuCateDTOS = menucateService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(menuCateDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", menuCateDTOS);
        return "menuCate/list";
    }




    @GetMapping("/menuCate/modify/{menuCateIdx}")
    public String modifyForm(@PathVariable Long menuCateIdx, Model model) {

        log.info("menuCate modifyProc 도착 " + menuCateIdx);

        MenuCateDTO menuCateDTO = menucateService.read(menuCateIdx);

        log.info("수정 전 정보" + menuCateDTO);
        model.addAttribute("menuCateDTO", menuCateDTO);
        return "menuCate/modify";
    }


    @PostMapping("/menuCate/modify/{menuCateIdx}")
    public String modifyProc(@Validated MenuCateDTO menuCateDTO,
                             BindingResult bindingResult, Model model) {

        log.info("menuCate modifyProc 도착 " + menuCateDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/menuCate/modify";
        }


        menucateService.modify(menuCateDTO);

        log.info("업데이트 이후 정보 " + menuCateDTO);

        return "redirect:/admin/distchief/store/"+ menuCateDTO.getTblStoreStoreIdx();
    }

    @GetMapping("/menuCate/delete/{menuCateIdx}")
    public String deleteProc(@PathVariable Long menuCateIdx) {

        menucateService.delete(menuCateIdx);

        return "redirect:/menuCate/list";
    }

    @GetMapping("/menuCate/{menuCateIdx}")
    public String readForm(@PathVariable Long menuCateIdx, Model model) {
        MenuCateDTO menuCateDTO=menucateService.read(menuCateIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("menuCateDTO",menuCateDTO);
        return "menuCate/read";
    }
}
