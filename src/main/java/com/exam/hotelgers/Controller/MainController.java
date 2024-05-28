package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import com.exam.hotelgers.service.ImageService;
import com.exam.hotelgers.service.ManagerService;
import com.exam.hotelgers.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {


    private final AdminRepository adminRepository;
    private final DistChiefRepository distChiefRepository;
    private final ManagerRepository managerRepository;
    private final SearchService searchService;
    private final ImageService imageService;
    private final ManagerService managerService;


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/admin")
    public String adminindex(){
        return "adminindex";
    }


    @GetMapping("/admin/login")
    public String adminLogin() {

        log.info("admin login 겟매핑 들어옴");


        return "admin/login";
    }


    @GetMapping("/member/login")
    public String memberLogin(Model model) {

        log.info("member login 겟매핑 들어옴");


        return "member/login";
    }




    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {

        log.info("logout 겟매핑 들어옴");


        session.invalidate(); //섹션 삭제
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        log.info("logout 겟매핑 들어옴");


        session.invalidate(); //섹션 삭제
        return "redirect:/";
    }




    @GetMapping("/login/proc")
    public String loginProc(Principal principal) {

        log.info("logout 겟매핑 들어옴");




        return "redirect:/";
    }




    @GetMapping("/layouts/main")
    public void mainForm(Principal principal, Model model){

        String userName = searchService.findByIdSendName(principal);

        model.addAttribute("userName",userName);

    }


    @GetMapping("/layouts/adminlayout")
    public void adminForm(Principal principal, Model model){


        String adminId = principal.getName();
        log.info("어드민아이디 추출 : " + adminId);

        Optional<Admin> admin = adminRepository.findByAdminId(adminId);

        String adminName = admin.get().getAdminName();
        log.info("어드민이름추출 : " + adminName);

        model.addAttribute("adminName",adminName);

    }



    //이미지 삭제
    @GetMapping("/deleteimg/{imageIdx}")
    public String deleteProc(@PathVariable Long imageIdx, HttpServletRequest request) {


        imageService.delete(imageIdx);

        // Referer 헤더를 가져옴
        String referer = request.getHeader("referer");

        // Referer 헤더가 null이 아니고 빈 문자열이 아닌 경우에만 해당 페이지로 리다이렉트
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            // Referer 헤더가 없을 경우 기본적으로 설정된 URL로 리다이렉트
            return "redirect:/admin/admin/banner/list";
        }
    }

    @GetMapping("/oauth2/authorization/google")
    public String googleProc(){
        return "oauth2/authorization/google";
    }



}