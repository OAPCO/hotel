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



    //다중검색 포스트매핑 메소드.
    //뷰에서 name으로 보낸 distName,branchName 등을 리퀘스트파람으로 받아서 저장합니다.
    @PostMapping("/distchief/order/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="branchName", required = false) String branchName,
                           @RequestParam(value="storeName", required = false) String storeName,
                           @RequestParam(value="storePType", required = false) StorePType storePType,
                           @RequestParam(value="storeStatus", required = false) StoreStatus storeStatus
    ){

        log.info("order listProc 도착 ");


        //서비스에 만들어둔 다중검색(메소드명 searchList) 메소드의 파라미터 값에
        //퀘스트파람으로 받아온 값을 넣어서 실행합니다.
        Page<OrderDTO> orderDTOS = orderService.searchList(distName,branchName,storeName,storePType,storeStatus,pageable);


        //셀렉트박스에 출력 할 총판,지사,매장 리스트를 "searchService"에서 가져옵니다.
        //order가 참조하는 테이블들의 모든 목록을 order의 인덱스 값 등으로 조회할 수 없으므로 필요한 코드입니다.
        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<StoreDTO> storeList = searchService.storeList();

        
        //orderDTOS에 페이지정보 담기
        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);


        //모델에 값을 담아서 뷰(html)로 보냅니다. addAttribute("뷰에서 쓸 변수명", 현재 가지고 있는 변수)
        //여기서 앞 큰따옴표 안에 넣은 변수명을 뷰에서 사용할 수 있습니다.
        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("list", orderDTOS);
        return "distchief/order/list";
    }

    @GetMapping("/distchief/order/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model
                           ) {

        log.info("order listForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);
        return "distchief/order/list";
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











    //이 밑은 신경 안쓰셔도 됩니다
    @GetMapping("/distchief/order/orderlist")
    public String orderlistForm(@PageableDefault(page = 1) Pageable pageable, Model model
    ) {

        log.info("order orderlistForm 도착 ");

        Page<OrderDTO> orderDTOS = orderService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);


        return "distchief/order/orderlist";
    }



    @PostMapping("/distchief/order/orderlist")
    public String orderlistProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="branchName", required = false) String branchName,
                           @RequestParam(value="storeName", required = false) String storeName,
                           @RequestParam(value="orderCd", required = false) String orderCd,
                           @RequestParam(value="roomCd", required = false) String roomCd ) {


        log.info("들어온 총판 @@@@@ + " + distName);
        log.info("들어온 총판 @@@@@ + " + branchName);
        log.info("들어온 총판 @@@@@ + " + storeName);
        log.info("들어온 총판 @@@@@ + " + orderCd);
        log.info("들어온 총판 @@@@@ + " + roomCd);



        Page<OrderDTO> orderDTOS = orderService.searchOrderList(distName,branchName,storeName,orderCd,roomCd,pageable);




        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(orderDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("storeList",storeList);
        model.addAttribute("roomList",roomList);
        model.addAttribute("list", orderDTOS);

        return "distchief/order/orderlist";
    }



    @GetMapping("/distchief/order/orderManagement")
    public void asdasd(){

    }

}
