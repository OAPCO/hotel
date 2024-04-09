package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.ManagerDTO;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.service.ManagerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ManagerController {


    private final ManagerService managerService;




    @GetMapping("/manager/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("manager listForm 도착");

        Page<ManagerDTO> managerDTOList = managerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(managerDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", managerDTOList);
        return "manager/list";
    }


    @GetMapping("/manager/register")
    public void registerForm() {

    }


    @PostMapping("/manager/register")
    public String registerProc(@Valid ManagerDTO managerDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("manager registerProc 도착");


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(managerDTO);

        Long managerIdx = managerService.register(managerDTO);

        redirectAttributes.addFlashAttribute("result", managerIdx);

        return "redirect:/manager/list";
    }





    @GetMapping("/manager/delete/{managerIdx}")
    public String deleteProc(@PathVariable Long managerIdx) {


        managerService.delete(managerIdx);
        //서비스처리(삭제)
        return "redirect:/manager/list";
    }






}
