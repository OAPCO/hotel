package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.service.AdminService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpSession;
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
public class AdminController {


    private final AdminService adminService;


    @GetMapping("/admin/login")
    public String login() {

        log.info("/login 겟매핑 들어옴");

        return "admin/login";
    }


    @GetMapping("/admin/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("admin listForm 도착");

        Page<AdminDTO> adminDTOList = adminService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(adminDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", adminDTOList);
        return "admin/list";
    }

    @PostMapping("/admin/list")
    public void listProc() {


    }

    @GetMapping("/admin/register")
    public void registerForm() {

    }


    @PostMapping("/admin/register")
    public String registerProc(@Valid AdminDTO adminDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("admin registerProc 도착");


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(adminDTO);

        Long adminIdx = adminService.register(adminDTO);

        redirectAttributes.addFlashAttribute("result", adminIdx);

        return "redirect:/admin/list";
    }





    @GetMapping("/admin/delete/{adminIdx}")
    public String deleteProc(@PathVariable Long adminIdx) {


        adminService.delete(adminIdx);
        //서비스처리(삭제)
        return "redirect:/admin/list";
    }

}
