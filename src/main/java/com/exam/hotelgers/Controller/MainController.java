package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import com.exam.hotelgers.service.SearchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {


    AdminRepository adminRepository;
    DistChiefRepository distChiefRepository;
    ManagerRepository managerRepository;
    SearchService searchService;


    @GetMapping("/")
    public String start(){
        return "index";
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


}