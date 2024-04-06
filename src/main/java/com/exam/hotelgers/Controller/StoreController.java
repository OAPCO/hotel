package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.StoreService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/manager")
public class StoreController {
    
    private final StoreService storeService;



    @GetMapping("/store/register")
    public String register() {
        return "manager/store/register";
    }


    @PostMapping("/store/register")
    public String registerProc(@Valid StoreDTO storeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("store registerProc 도착 " + storeDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long storeIdx = storeService.register(storeDTO);

        log.info("열거형확인@@@@@type는 " + storeDTO.getStorePType());
        log.info("열거형확인@@@@@status는 " + storeDTO.getStoreStatus());


        redirectAttributes.addFlashAttribute("result", storeIdx);

        return "redirect:/manager/store/list";
    }


    @GetMapping("/store/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("store listForm 도착 ");

        Page<StoreDTO> storeDTOS = storeService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", storeDTOS);
        return "manager/store/list";
    }


    @GetMapping("/store/order")
    public String orderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("store orderForm 도착 ");

        Page<StoreDTO> storeDTOS = storeService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", storeDTOS);
        return "manager/store/order";
    }




    @GetMapping("/store/modify/{storeIdx}")
    public String modifyForm(@PathVariable Long storeIdx, Model model) {

        log.info("store modifyProc 도착 " + storeIdx);

        StoreDTO storeDTO = storeService.read(storeIdx);

        log.info("수정 전 정보" + storeDTO);
        model.addAttribute("storeDTO", storeDTO);
        return "manager/store/modify";
    }


    @PostMapping("/store/modify")
    public String modifyProc(@Validated StoreDTO storeDTO,
                             BindingResult bindingResult, Model model) {

        log.info("store modifyProc 도착 " + storeDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "manager/store/modify";
        }


        storeService.modify(storeDTO);

        log.info("업데이트 이후 정보 " + storeDTO);

        return "redirect:/manager/store/list";
    }

    @GetMapping("/store/delete/{storeIdx}")
    public String deleteProc(@PathVariable Long storeIdx) {

        storeService.delete(storeIdx);

        return "redirect:/manager/store/list";
    }

    @GetMapping("/store/{storeIdx}")
    public String readForm(@PathVariable Long storeIdx, Model model) {
        StoreDTO storeDTO=storeService.read(storeIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("storeDTO",storeDTO);
        return "manager/store/read";
    }



}
