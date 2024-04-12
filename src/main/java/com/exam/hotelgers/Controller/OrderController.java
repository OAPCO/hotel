package com.exam.hotelgers.Controller;


import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.ImageService;
import com.exam.hotelgers.service.OrderService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.OrderService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2

public class OrderController {
    
    private final OrderService orderService;
    private final SearchService searchService;





    @GetMapping("/order/register")
    public String register() {
        return "manager/order/register";
    }


    @PostMapping("/order/register")
    public String registerProc(@Valid OrderDTO orderDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("order registerProc 도착 " + orderDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long orderIdx = orderService.register(orderDTO);
        orderDTO.setOrderIdx(orderIdx);

        


        redirectAttributes.addFlashAttribute("result", orderIdx);

        return "redirect:/manager/order/list";
    }


    @PostMapping("/order/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="branchName", required = false) String branchName,
                           @RequestParam(value="orderName", required = false) String orderName,
                           @RequestParam(value="orderPType", required = false) StorePType orderPType,
                           @RequestParam(value="orderStatus", required = false) StoreStatus orderStatus
                           ){


        log.info("들어온 상태 값 : @@ + " + orderStatus);
        log.info("들어온 피타입 값 : @@ + " + orderPType);



        Page<OrderDTO> orderDTOS = orderService.searchList(distName,branchName,orderName,orderPType,orderStatus,pageable);



        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();
        List<StoreDTO> storeList = searchService.storeList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("list", orderDTOS);
        return "manager/order/list";
    }

    @GetMapping("/order/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("order listForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", orderDTOS);
        return "manager/order/list";
    }


    @GetMapping("/order/order")
    public String orderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("order orderForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", orderDTOS);
        return "manager/order/order";
    }




    @GetMapping("/order/modify/{orderIdx}")
    public String modifyForm(@PathVariable Long orderIdx, Model model) {

        log.info("order modifyProc 도착 " + orderIdx);

        OrderDTO orderDTO = orderService.read(orderIdx);

        log.info("수정 전 정보" + orderDTO);
        model.addAttribute("orderDTO", orderDTO);
        return "manager/order/modify";
    }


    @PostMapping("/order/modify")
    public String modifyProc(@Validated OrderDTO orderDTO,
                             BindingResult bindingResult, Model model) {

        log.info("order modifyProc 도착 " + orderDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "manager/order/modify";
        }


        orderService.modify(orderDTO);

        log.info("업데이트 이후 정보 " + orderDTO);

        return "redirect:/manager/order/list";
    }

    @GetMapping("/order/delete/{orderIdx}")
    public String deleteProc(@PathVariable Long orderIdx) {

        orderService.delete(orderIdx);

        return "redirect:/manager/order/list";
    }

    @GetMapping("/order/{orderIdx}")
    public String readForm(@PathVariable Long orderIdx, Model model) {
        OrderDTO orderDTO=orderService.read(orderIdx);


        model.addAttribute("orderDTO",orderDTO);
        return "manager/order/read";
    }


}
