package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.OrderService;
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

import java.util.List;
import java.util.Map;



@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    
    private final OrderService orderService;
    private final SearchService searchService;



    @GetMapping("/manager/order/register")
    public String register() {
        return "manager/order/register";
    }


    @PostMapping("/manager/order/register")
    public String registerProc(@Valid OrderDTO orderDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("order registerProc 도착 " + orderDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long orderIdx = orderService.register(orderDTO);


        redirectAttributes.addFlashAttribute("result", orderIdx);

        return "redirect:/manager/order/list";
    }



    @PostMapping("/admin/distchief/order/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="storeName", required = false) String storeName,
                           @RequestParam(value="storePType", required = false) StorePType storePType,
                           @RequestParam(value="storeStatus", required = false) StoreStatus storeStatus
    ){

        log.info("order listProc 도착 ");


        Page<OrderDTO> orderDTOS = orderService.searchList(distName,storeName,storePType,storeStatus,pageable);


        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);


        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("list", orderDTOS);
        return "admin/distchief/order/list";
    }

    @GetMapping("/admin/distchief/order/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model
                           ) {

        log.info("order listForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);
        return "admin/distchief/order/list";
    }




    @GetMapping("/manager/order/modify/{orderIdx}")
    public String modifyForm(@PathVariable Long orderIdx, Model model) {

        log.info("order modifyProc 도착 " + orderIdx);

        OrderDTO orderDTO = orderService.read(orderIdx);

        log.info("수정 전 정보" + orderDTO);
        model.addAttribute("orderDTO", orderDTO);
        return "manager/order/modify";
    }


    @PostMapping("/manager/order/modify")
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





    @GetMapping("/manager/order/delete/{orderIdx}")
    public String deleteProc(@PathVariable Long orderIdx) {

        orderService.delete(orderIdx);

        return "redirect:/manager/order/list";
    }



    @GetMapping("/manager/order/{orderIdx}")
    public String readForm(@PathVariable Long orderIdx, Model model) {
        OrderDTO orderDTO=orderService.read(orderIdx);


        model.addAttribute("data",orderDTO);
        return "manager/order/read";
    }











    @GetMapping("/admin/distchief/order/orderlist")
    public String orderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model
    ) {

        log.info("order orderlistForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);


        return "admin/distchief/order/orderlist";
    }



    @PostMapping("/admin/distchief/order/orderlist")
    public String orderlistProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="storeName", required = false) String storeName,
                           @RequestParam(value="orderCd", required = false) String orderCd,
                           @RequestParam(value="roomCd", required = false) String roomCd ) {


        log.info("들어온 총판 @@@@@ + " + distName);
        log.info("들어온 총판 @@@@@ + " + storeName);
        log.info("들어온 총판 @@@@@ + " + orderCd);
        log.info("들어온 총판 @@@@@ + " + roomCd);



        Page<OrderDTO> orderDTOS = orderService.searchOrderList(distName,storeName,orderCd,roomCd,pageable);




        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);

        return "admin/distchief/order/orderlist";
    }



    @GetMapping("/admin/distchief/order/orderManagement")
    public void asdasd(){

    }

}
