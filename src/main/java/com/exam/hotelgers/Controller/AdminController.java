package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final DistService distService;
    private final SearchService searchService;
    private final DistChiefService distChiefService;
    private PasswordEncoder passwordEncoder;
    private final QnaService qnaService;




    @GetMapping("/admin/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("admin listForm 도착");

        Page<AdminDTO> adminDTOList = adminService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(adminDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", adminDTOList);
        return "admin/list";
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





    @GetMapping("/admin/admin/manage/memberlist")
    public String memberlistForm(@PageableDefault(page = 1)Pageable pageable,Model model) throws Exception{

        Page<Object> objects = adminService.memberList(pageable);

        log.info(objects);

        Map<String, Integer> pageinfo = PageConvert.Pagination(objects);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", objects);

        return "admin/admin/manage/memberlist";
    }





    @PostMapping("/admin/admin/manage/memberlist")
    public String memberlistProc(@PageableDefault(page = 1)Pageable pageable,Model model,SearchDTO searchDTO) throws Exception{

        Page<Object> objects = adminService.memberListSearch(pageable,searchDTO);

        log.info(objects);

        Map<String, Integer> pageinfo = PageConvert.Pagination(objects);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", objects);

        return "admin/admin/manage/memberlist";
    }



    @GetMapping("/admin/adminpage/distregister")
    public String distregister(@PageableDefault(page = 1)Pageable pageable,Model model){
        log.info("storemangelist 도착");

        Page<DistDTO> distDTOList = distService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distDTOList);
        return "admin/adminpage/distregister";
    }



    @PostMapping("/admin/adminpage/distregister")
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

        return "redirect:/admin/adminpage/storedistmange";
    }







    @GetMapping("/admin/admin/manage/qnalist")
    public String qnaListForm(@PageableDefault(page = 1)Pageable pageable,Model model) throws Exception{

        Page<QnaDTO> qnaDTOS = qnaService.list(pageable);

        model.addAttribute("list", qnaDTOS);

        return "admin/admin/manage/qnalist";
    }



    @PostMapping("/admin/admin/manage/qnalist")
    public String qnaListProc(@PageableDefault(page = 1)Pageable pageable,Model model,SearchDTO searchDTO) throws Exception{

        Page<QnaDTO> qnaDTOS = qnaService.getQna(pageable,searchDTO);

        Map<String, Integer> pageinfo = PageConvert.Pagination(qnaDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", qnaDTOS);

        return "admin/admin/manage/qnalist";
    }



    @PostMapping("/admin/admin/manage/answer")
    public String answerProc(@PageableDefault(page = 1)Pageable pageable,Model model,SearchDTO searchDTO) throws Exception{

        log.info(searchDTO.getQnaIdx());
        log.info(searchDTO.getQnaAnswer());

        qnaService.answerUpdate(searchDTO);

        return "redirect:/admin/admin/manage/qnalist";

    }
    @GetMapping("/admin/admin/pwchange")
    public String pwchangeForm() {

        return "admin/admin/pwchange";
    }

    @PostMapping("/admin/admin/pwchange")
    public String pwchangeproc(Principal principal, String currentPassword, String newPassword,Model model,RedirectAttributes redirectAttributes) {

        log.info("현재비밀번호"+currentPassword+"새비밀번호"+newPassword);
        int result=adminService.changePassword(currentPassword,newPassword,principal);
        log.info("결과값"+result);
        if (result==0) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "admin/admin/pwchange";
        }

        redirectAttributes.addFlashAttribute("successMessage", "비밀번호 변경이 완료되었습니다.");
        return "redirect:/admin/admin/pwchange";
    }
}

