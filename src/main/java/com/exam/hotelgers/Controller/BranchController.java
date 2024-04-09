package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.branchDTO;
import com.exam.hotelgers.service.BranchService;
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
public class BranchController {
    
    private final BranchService branchService;



    @GetMapping("/branch/register")
    public String register() {
        return "branch/register";
    }


    @PostMapping("/branch/register")
    public String registerProc(@Valid branchDTO branchDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("branch registerProc 도착 " + branchDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long branchIdx = branchService.register(branchDTO);

        redirectAttributes.addFlashAttribute("result", branchIdx);

        return "redirect:/branch/list";
    }


    @GetMapping("/branch/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("branch listForm 도착 ");

        Page<branchDTO> branchDTOS = branchService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(branchDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", branchDTOS);



        return "branch/list";
    }




    @GetMapping("/branch/modify/{branchIdx}")
    public String modifyForm(@PathVariable Long branchIdx, Model model) {

        log.info("branch modifyProc 도착 " + branchIdx);

        branchDTO branchDTO = branchService.read(branchIdx);

        log.info("수정 전 정보" + branchDTO);
        model.addAttribute("branchDTO", branchDTO);
        return "branch/modify";
    }


    @PostMapping("/branch/modify")
    public String modifyProc(@Validated branchDTO branchDTO,
                             BindingResult bindingResult, Model model) {

        log.info("branch modifyProc 도착 " + branchDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/branch/modify";
        }


        branchService.modify(branchDTO);

        log.info("업데이트 이후 정보 " + branchDTO);

        return "redirect:/branch/list";
    }

    @GetMapping("/branch/delete/{branchIdx}")
    public String deleteProc(@PathVariable Long branchIdx) {

        branchService.delete(branchIdx);

        return "redirect:/branch/list";
    }

    @GetMapping("/branch/{branchIdx}")
    public String readForm(@PathVariable Long branchIdx, Model model) {
        branchDTO branchDTO=branchService.read(branchIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("branchDTO",branchDTO);
        return "branch/read";
    }
}
