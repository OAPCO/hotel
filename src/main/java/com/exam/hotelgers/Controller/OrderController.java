package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.entity.MenuOrder;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.ManagerRepository;
import com.exam.hotelgers.repository.RewardRepository;
import com.exam.hotelgers.repository.StoreRepository;
import com.exam.hotelgers.service.*;

import com.exam.hotelgers.util.PageConvert;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    
    private final MenuOrderService menuOrderService;
    private final SearchService searchService;
    private final ManagerService managerService;
    private final MemberpageService memberpageService;
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;
    private final RewardService rewardService;
    private  final CouponService couponService;

//    @GetMapping("/admin/manager/order/register")
//    public String register() {
//        return "admin/manager/order/register";
//    }


    @PostMapping("/admin/manager/order/register")
    public String registerProc(@Valid MenuOrderDTO orderDTO,
                               @Nullable @RequestParam Long couponIdx,
                               @Nullable @ModelAttribute RewardDTO rewardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("order registerProc 도착 " + orderDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long menuorderIdx = menuOrderService.register(orderDTO);

        if(rewardDTO != null) {
            rewardService.register(rewardDTO);
            log.info(rewardDTO +  "페이지에서 보내주는 리워드 DTO");
        }

        couponService.useCoupon(couponIdx);
        log.info(couponIdx +  "페이지에서 보내주는 couponIdx");
        redirectAttributes.addFlashAttribute("result", menuorderIdx);

        return "redirect:/member/memberpage/index";
    }



//    @GetMapping("/admin/distchief/order/list")
//    public String listProc(Model model, Principal principal){
//        Authentication authentication = (Authentication) principal;
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .anyMatch(role -> role.equals(RoleType.ADMIN.name()));
//
//        if (isAdmin) {
//            Optional<List<Store>> storesOpt = managerService.findStoresByManager(managerIdx);
//            if (storesOpt.isPresent()) {
//                List<Store> stores = storesOpt.get();
//                model.addAttribute("stores", stores);
//            }
//        }
//
//        return "admin/distchief/order/list";
//    }



    @GetMapping("/admin/manager/order/menuorderlist")
    public String listForm(Principal principal, Model model) {
        log.info("order listForm 도착 ");

        String username = principal.getName(); // assuming the principal's name is the username
        Optional<Manager> managerOpt = managerRepository.findByManagerId(username);

        if (managerOpt.isPresent()) {
            Optional<Store> storeOpt = storeRepository.findByManagerId(username);


            if (storeOpt.isPresent()) {
                Store store = storeOpt.get();
                OrderHistoryDTO orderHistoryDTO = menuOrderService.getOrderHistoryByStore(store.getStoreIdx());
                log.info(store.getStoreIdx() + "스토어 Idx !!!!!!!!!!!!!!!!!!!!!!");
                model.addAttribute("storeName", store.getStoreName());
                model.addAttribute("menuOrderDetailList", orderHistoryDTO.getMenuOrderDetailList());
                model.addAttribute("roomOrderDetailList", orderHistoryDTO.getRoomOrderDetailList());
            }
        }

        return "admin/manager/order/menuorderlist";
    }


    @GetMapping("/admin/manager/order/modify/{menuorderIdx}")
    public String modifyForm(@PathVariable Long menuorderIdx, Model model) {

        log.info("order modifyProc 도착 " + menuorderIdx);

        MenuOrderDTO menuOrderDTO = menuOrderService.read(menuorderIdx);

        log.info("수정 전 정보" + menuOrderDTO);
        model.addAttribute("orderDTO", menuOrderDTO);
        return "admin/manager/order/modify";
    }


    @PostMapping("/admin/manager/order/modify")
    public String modifyProc(@Validated MenuOrderDTO orderDTO,
                             BindingResult bindingResult, Model model) {

        log.info("order modifyProc 도착 " + orderDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/manager/order/modify";
        }


        menuOrderService.modify(orderDTO);

        log.info("업데이트 이후 정보 " + orderDTO);

        return "redirect:/admin/manager/order/list";
    }





    @GetMapping("/admin/manager/order/delete/{menuorderIdx}")
    public String deleteProc(@PathVariable Long menuorderIdx) {

        menuOrderService.delete(menuorderIdx);

        return "redirect:/admin/manager/order/list";
    }



    @GetMapping("/admin/manager/order/{menuorderIdx}")
    public String readForm(@PathVariable Long menuorderIdx, Model model) {
        MenuOrderDTO menuOrderDTO=menuOrderService.read(menuorderIdx);


        model.addAttribute("data",menuOrderDTO);
        return "admin/manager/order/read";
    }











    @GetMapping("/admin/distchief/order/orderlist")
    public String distchiefOrderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model
    ) {

        log.info("order orderlistForm 도착 ");

        Page<MenuOrderDTO> menuOrderDTOS = menuOrderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(menuOrderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", menuOrderDTOS);


        return "admin/distchief/order/orderlist";
    }



//    @PostMapping("/admin/distchief/order/orderlist")
//    public String distchiefOrderlistProc(@PageableDefault(page = 1) Pageable pageable, Model model,
//                           @RequestParam(value="distName", required = false) String distName,
//                           @RequestParam(value="storeName", required = false) String storeName,
//                           @RequestParam(value="orderCd", required = false) String orderCd,
//                           @RequestParam(value="roomCd", required = false) String roomCd ) {
//
//
//        log.info("들어온 총판 @@@@@ + " + distName);
//        log.info("들어온 총판 @@@@@ + " + storeName);
//        log.info("들어온 총판 @@@@@ + " + orderCd);
//        log.info("들어온 총판 @@@@@ + " + roomCd);
//
//
//
//        Page<OrderDTO> orderDTOS = orderService.searchOrderList(distName,storeName,orderCd,roomCd,pageable);
//
//
//
//
//        List<DistDTO> distList = searchService.distList();
//        List<StoreDTO> storeList = searchService.storeList();
//        List<RoomDTO> roomList = searchService.roomList();
//
//
//        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("distList",distList);
//        model.addAttribute("storeList",storeList);
//        model.addAttribute("roomList",roomList);
//        model.addAttribute("list", orderDTOS);
//
//        return "admin/distchief/order/orderlist";
//    }



    @GetMapping("/admin/distchief/order/orderManagement")
    public void asdasd(){

    }
    @GetMapping("/order/orderManagement")
    public String orderManagementForm(@PageableDefault(page = 1) Pageable pageable, Model model
    ) {

        log.info("order orderlistForm 도착 ");

        Page<MenuOrderDTO> orderDTOS = menuOrderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);


        return "/order/orderManagement";
    }



//    @PostMapping("/order/orderManagement")
//    public String orderorderManagementProc(@PageableDefault(page = 1) Pageable pageable, Model model,
//                                @RequestParam(value="distName", required = false) String distName,
//                                @RequestParam(value="storeName", required = false) String storeName,
//                                @RequestParam(value="orderCd", required = false) String orderCd,
//                                @RequestParam(value="roomCd", required = false) String roomCd ) {
//
//
//        log.info("들어온 총판 @@@@@ + " + distName);
//        log.info("들어온 총판 @@@@@ + " + storeName);
//        log.info("들어온 총판 @@@@@ + " + orderCd);
//        log.info("들어온 총판 @@@@@ + " + roomCd);
//
//
//
//        Page<OrderDTO> orderDTOS = orderService.searchOrderList(distName,storeName,orderCd,roomCd,pageable);
//
//
//
//
//        List<DistDTO> distList = searchService.distList();
//        List<StoreDTO> storeList = searchService.storeList();
//        List<RoomDTO> roomList = searchService.roomList();
//
//
//        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("distList",distList);
//        model.addAttribute("storeList",storeList);
//        model.addAttribute("roomList",roomList);
//        model.addAttribute("list", orderDTOS);
//
//        return "/order/orderManagement";
//    }





    @GetMapping("/admin/manager/order/select")
    public String managerOrderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model, Principal principal
    ) {

        log.info("매니저 order/select Form 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);
        DistDTO distDTO = managerService.managerOfDist(principal);
        List<RoomDTO> roomDTOS = managerService.managerOfLoom(principal);

        Page<MenuOrderDTO> menuOrderDTOS = menuOrderService.list(pageable);



        Map<String, Integer> pageinfo = PageConvert.Pagination(menuOrderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distDTO",distDTO);
        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("roomList",roomDTOS);
        model.addAttribute("orderlist", menuOrderDTOS);


        return "admin/manager/order/select";
    }

}
