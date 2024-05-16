package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }


    @PostMapping("/member/register")
    public String registerProc(@Valid MemberDTO memberDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("member registerProc 도착 " + memberDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(memberDTO);

        Long memberIdx = memberService.register(memberDTO);

        redirectAttributes.addFlashAttribute("result", memberIdx);

        return "redirect:/member/memberpage/index";
    }


    @GetMapping("/member/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("member listForm 도착 ");

        Page<MemberDTO> memberDTOS = memberService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(memberDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", memberDTOS);
        return "member/list";
    }




    @GetMapping("/member/modify/{memberIdx}")
    public String modifyForm(@PathVariable Long memberIdx, Model model) {

        log.info("member modifyForm 도착 번호 : " + memberIdx);

        MemberDTO memberDTO = memberService.read(memberIdx);

        log.info("수정 전 정보" + memberDTO);
        model.addAttribute("memberDTO", memberDTO);
        return "member/modify";
    }


    @PostMapping("/member/modify")
    public String modifyProc(@Validated MemberDTO memberDTO,
                             BindingResult bindingResult, Model model) {

        log.info("member modifyProc 도착 " + memberDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/member/modify";
        }


        memberService.modify(memberDTO);

        log.info("업데이트 이후 정보 " + memberDTO);

        return "redirect:/member/list";
    }

    @GetMapping("/member/delete/{memberIdx}")
    public String deleteProc(@PathVariable Long memberIdx) {

        memberService.delete(memberIdx);

        return "redirect:/member/list";
    }

    @GetMapping("/member/{memberIdx}")
    public String readForm(@PathVariable Long memberIdx, Model model) {
        MemberDTO memberDTO=memberService.read(memberIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("memberDTO",memberDTO);
        return "member/read";
    }





}
