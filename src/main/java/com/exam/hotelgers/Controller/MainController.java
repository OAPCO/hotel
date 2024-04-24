package com.exam.hotelgers.Controller;



import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {


    AdminRepository adminRepository;

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
    public String memberLogin() {

        log.info("member login 겟매핑 들어옴");

        return "member/login";
    }


    @GetMapping("/admin/manager/login")
    public String managerLogin() {

        log.info("manager login 겟매핑 들어옴");

        return "admin/manager/login";
    }

    @GetMapping("/admin/distchief/login")
    public String distchiefLogin() {

        log.info("distchief login 겟매핑 들어옴");

        return "admin/distchief/login";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {

        log.info("logout 겟매핑 들어옴");


        session.invalidate(); //섹션 삭제
        return "redirect:/";
    }


    @GetMapping("/layouts/main")
    public void mainForm(){



    }


    @GetMapping("/layouts/adminlayout")
    public void adminForm(Principal principal, Model model){


        String adminId = principal.getName();

        Optional<Admin> admin = adminRepository.findByAdminId(adminId);

        String adminName = admin.get().getAdminName();

        model.addAttribute("adminName",adminName);


    }


}