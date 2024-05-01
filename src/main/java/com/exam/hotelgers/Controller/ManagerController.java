package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.ManagerDTO;
import com.exam.hotelgers.service.DistChiefService;
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

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ManagerController {


    private final ManagerService managerService;
    private final DistChiefService distChiefService;




    @GetMapping("/admin/manager/list")
    public String listForm123(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("manager listForm 도착");

        Page<ManagerDTO> managerDTOList = managerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(managerDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", managerDTOList);
        return "admin/manager/list";
    }


    @GetMapping("/admin/manager/register")
    public void registerForm123() {

    }


    @PostMapping("/admin/manager/register")
    public String registerProc123(@Valid ManagerDTO managerDTO,
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

        return "redirect:/admin/manager/list";
    }





    @GetMapping("/admin/distchief/manager/delete/{managerIdx}")
    public String deleteProc(@PathVariable Long managerIdx) {
        
        log.info("매니저 삭제 Proc 들어옴");


        managerService.delete(managerIdx);
        //서비스처리(삭제)
        return "redirect:/admin/distchief/manager/list";
    }


    @GetMapping("/admin/distchief/manager/register")
    public String registerForm(Principal principal,Model model) {

        List<DistDTO> distDTOS = distChiefService.distChiefOfDistList(principal);
        log.info("총판목록 들어왓나@@@@@@@@@@@@@@@@@@@@@@@ + " + distDTOS);

        model.addAttribute("distDTOS",distDTOS);

        return "admin/distchief/manager/register";

    }


    @PostMapping("/admin/distchief/manager/register")
    public String registerProc(@Valid ManagerDTO managerDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        log.info("매니저생성 프록 도착 " + managerDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        Long managerIdx = managerService.register(managerDTO);



        redirectAttributes.addFlashAttribute("result", managerIdx);

        return "redirect:/admin/distchief/manager/list";
    }

    @GetMapping("/admin/distchief/manager/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("매장주 listchiefForm 도착 ");

        Page<ManagerDTO> managerDTOS = managerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(managerDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", managerDTOS);


        return "admin/distchief/manager/list";
    }


}
