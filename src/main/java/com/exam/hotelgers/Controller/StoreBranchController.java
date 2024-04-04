package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.StoreBranchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.StoreBranchService;
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

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StoreBranchController {
    
    private final StoreBranchService storebranchService;



    @GetMapping("/storebranch/register")
    public String register() {
        return "storebranch/register";
    }


    @PostMapping("/storebranch/register")
    public String registerProc(@Valid StoreBranchDTO storebranchDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("storebranch registerProc 도착 " + storebranchDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long storeBranchIdx = storebranchService.register(storebranchDTO);

        redirectAttributes.addFlashAttribute("result", storeBranchIdx);

        return "redirect:/storebranch/list";
    }


    @GetMapping("/storebranch/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("storebranch listForm 도착 ");

        Page<StoreBranchDTO> storebranchDTOS = storebranchService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(storebranchDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", storebranchDTOS);



        return "storebranch/list";
    }




    @GetMapping("/storebranch/modify/{storebranchIdx}")
    public String modifyForm(@PathVariable Long storebranchIdx, Model model) {

        log.info("storebranch modifyProc 도착 " + storebranchIdx);

        StoreBranchDTO storebranchDTO = storebranchService.read(storebranchIdx);

        log.info("수정 전 정보" + storebranchDTO);
        model.addAttribute("storebranchDTO", storebranchDTO);
        return "storebranch/modify";
    }


    @PostMapping("/storebranch/modify")
    public String modifyProc(@Validated StoreBranchDTO storebranchDTO,
                             BindingResult bindingResult, Model model) {

        log.info("storebranch modifyProc 도착 " + storebranchDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/storebranch/modify";
        }


        storebranchService.modify(storebranchDTO);

        log.info("업데이트 이후 정보 " + storebranchDTO);

        return "redirect:/storebranch/list";
    }

    @GetMapping("/storebranch/delete/{storebranchIdx}")
    public String deleteProc(@PathVariable Long storebranchIdx) {

        storebranchService.delete(storebranchIdx);

        return "redirect:/storebranch/list";
    }

    @GetMapping("/storebranch/{storebranchIdx}")
    public String readForm(@PathVariable Long storebranchIdx, Model model) {
        StoreBranchDTO storebranchDTO=storebranchService.read(storebranchIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("storeBranchDTO",storebranchDTO);
        return "storebranch/read";
    }
}
