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

    private final AdminRepository adminRepository;

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


    @GetMapping("/manager/login")
    public String managerLogin() {

        log.info("manager login 겟매핑 들어옴");

        return "manager/login";
    }

    @GetMapping("/distchief/login")
    public String distchiefLogin() {

        log.info("distchief login 겟매핑 들어옴");

        return "distchief/login";
    }

    @GetMapping("/branchchief/login")
    public String branchchiefLogin() {

        log.info("branchchief login 겟매핑 들어옴");

        return "branchchief/login";
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


        String userId = principal.getName();
        log.info("어드민아이디 추출 : " + userId);

        Optional<Admin> admin = adminRepository.findByAdminId(userId);

        String username = admin.get().getAdminName();
        log.info("어드민이름추출 : " + username);

        model.addAttribute("userid",userId);
        model.addAttribute("username",username);


    }

}
