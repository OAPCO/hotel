package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BranchChiefDTO;
import com.exam.hotelgers.dto.BranchDTO;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.service.BranchChiefService;
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
    private final BranchChiefService branchChiefService;



    @GetMapping("/branch/register")
    public String register() {
        return "branch/register";
    }


    @PostMapping("/branch/register")
    public String registerProc(@Valid BranchDTO branchDTO,
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

        Page<BranchDTO> branchDTOS = branchService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(branchDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", branchDTOS);



        return "branch/list";
    }




    @GetMapping("/branch/modify/{branchIdx}")
    public String modifyForm(@PathVariable Long branchIdx, Model model) {

        log.info("branch modifyProc 도착 " + branchIdx);

        BranchDTO branchDTO = branchService.read(branchIdx);

        log.info("수정 전 정보" + branchDTO);
        model.addAttribute("branchDTO", branchDTO);
        return "branch/modify";
    }


    @PostMapping("/branch/modify")
    public String modifyProc(@Validated BranchDTO branchDTO,
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
        BranchDTO branchDTO=branchService.read(branchIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("branchDTO",branchDTO);
        return "branch/read";
    }



    @GetMapping("/distchief/branch/list")
    public void branchListForm() {

    }


    @GetMapping("/distchief/branch/register")
    public void branchRegisterForm() {

    }

    @GetMapping("/distchief/branch/registerchief")
    public String ChiefRegisterForm() {

        return "distchief/branch/registerchief";

    }

    @GetMapping("/distchief/branch/listchief")
    public String ChiefListForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("branch listchiefForm 도착 ");

        Page<BranchChiefDTO> branchChiefDTOS = branchChiefService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(branchChiefDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", branchChiefDTOS);


        return "distchief/branch/listchief";
    }


    @PostMapping("/distchief/branch/registerchief")
    public String ChiefRegisterProc(@Valid BranchChiefDTO branchChiefDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("branchchief registerProc 도착 " + branchChiefDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long branchChiefIdx = branchChiefService.register(branchChiefDTO);

        redirectAttributes.addFlashAttribute("result", branchChiefIdx);

        return "redirect:/distchief/branch/listchief";
    }


    @GetMapping("/distchief/branch/deletechief/{branchChiefIdx}")
    public String chiefDeleteProc(@PathVariable Long branchChiefIdx) {

        branchChiefService.delete(branchChiefIdx);

        return "redirect:/distchief/branch/listchief";
    }

    //id 검색
    //전체목록
    @GetMapping("/distchief/branch/idsearch")
    public String idsearch(@PageableDefault(page=1) Pageable pageable, Model model) {
        log.info("서비스로 모든 데이터 조회....");

        //searchDTO : 조회항목들이 들어있는 DTO
        //입력받은 열거형값을 열거형 데이터로 변환한다.
        Page<BranchChiefDTO> branchChiefDTOS = branchChiefService.list(pageable);

        //페이지 정보 처리
        Map<String, Integer> pageinfo = PageConvert.Pagination(branchChiefDTOS);
        model.addAllAttributes(pageinfo);

        //결과데이터
        model.addAttribute("list", branchChiefDTOS);

        return "distchief/branch/idsearch";
    }

}
