package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.PaymentService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.StoreService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
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

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PaymentController {

    private final PaymentService paymentservice;
    private final DistService distService;
    private final StoreService storeService;
    private final DistChiefRepository distChiefRepository;
    private final SearchService searchService;



    @GetMapping("/payment/register")
    public String register() {
        return "payment/register";
    }


    @PostMapping("/payment/register")
    public String registerProc(@Valid PaymentDTO paymentDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("payment registerProc 도착 " + paymentDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(paymentDTO);

        Long paymentIdx = paymentservice.register(paymentDTO);

        redirectAttributes.addFlashAttribute("result", paymentIdx);

        return "redirect:/payment/list";
    }





    @GetMapping("/payment/modify/{paymentIdx}")
    public String modifyForm(@PathVariable Long paymentIdx, Model model) {

        log.info("payment modifyProc 도착 " + paymentIdx);

        PaymentDTO paymentDTO = paymentservice.read(paymentIdx);

        log.info("수정 전 정보" + paymentDTO);
        model.addAttribute("paymentDTO", paymentDTO);
        return "payment/modify";
    }


    @PostMapping("/payment/modify")
    public String modifyProc(@Validated PaymentDTO paymentDTO,
                             BindingResult bindingResult, Model model) {

        log.info("payment modifyProc 도착 " + paymentDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/payment/modify";
        }


        paymentservice.modify(paymentDTO);

        log.info("업데이트 이후 정보 " + paymentDTO);

        return "redirect:/payment/list";
    }

    @GetMapping("/payment/delete/{paymentIdx}")
    public String deleteProc(@PathVariable Long paymentIdx) {

        paymentservice.delete(paymentIdx);

        return "redirect:/payment/list";
    }

    @GetMapping("/payment/{paymentIdx}")
    public String readForm(@PathVariable Long paymentIdx, Model model) {
        PaymentDTO paymentDTO=paymentservice.read(paymentIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("paymentDTO",paymentDTO);
        return "payment/read";
    }




    @GetMapping("/admin/manager/storesales")
    public String storeSalesForm(@PageableDefault(page=1) Pageable pageable, Principal principal, Model model) throws Exception {

        StoreDTO storeDTO = storeService.searchStoreuserId(principal);

        Page<PaymentDTO> paymentDTOS = paymentservice.list(pageable, storeDTO.getStoreIdx());

        Object[][] yearlySales = paymentservice.getYearlySales(storeDTO.getStoreIdx());
        Object[][] monthSales = paymentservice.getMonthSales(storeDTO.getStoreIdx());
        Object[][] daySales = paymentservice.getDaySales(storeDTO.getStoreIdx());


        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeDTO", storeDTO);
        model.addAttribute("list", paymentDTOS);
        model.addAttribute("yearlySales", yearlySales);
        model.addAttribute("monthSales", monthSales);
        model.addAttribute("daySales", daySales);

        log.info("화긴222"+yearlySales);

        return "admin/manager/storesales";
    }




    @PostMapping("/admin/manager/storesales")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @Valid SearchDTO searchDTO, Principal principal
    ) throws Exception {

        log.info("스토어-"+searchDTO.getStoreIdx());
        log.info("날짜"+searchDTO.getStartLocalDate());
        log.info("날짜2"+searchDTO.getEndLocalDate());


        Page<PaymentDTO> paymentDTOS = paymentservice.searchList(pageable,searchDTO);

        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", paymentDTOS);


        return "admin/manager/storesales";
    }









    @GetMapping("/admin/distchief/dist/distsales")
    public String distSalesForm(@PageableDefault(page=1) Pageable pageable,
                                Principal principal, Model model,
                                @RequestParam(required = false,defaultValue = "") String startDate,
                                @RequestParam(required = false,defaultValue = "") String paymentStatus,
                                @RequestParam(required = false,defaultValue = "") String storeIdx,
                                @RequestParam(required = false,defaultValue = "") String distIdx
    ) throws Exception {

        log.info("여기서시작"+startDate);
        log.info(paymentStatus);
        log.info(storeIdx);
        log.info(distIdx);



        if (!startDate.isEmpty() || !storeIdx.isEmpty() || !distIdx.isEmpty()){

            SearchDTO searchDTO = new SearchDTO();

            if (!paymentStatus.isEmpty()) {
                int paymentI = Integer.parseInt(paymentStatus);
                searchDTO.setPaymentStatus(paymentI);
            }

            if (!distIdx.isEmpty()) {
                Long distIdxL = Long.parseLong(distIdx);
                searchDTO.setDistIdx(distIdxL);
            }

            if (!storeIdx.isEmpty()) {
                Long storeIdxL = Long.parseLong(storeIdx);
                searchDTO.setStoreIdx(storeIdxL);
            }

            if (!startDate.isEmpty()) {
                searchDTO.setStartDate(startDate);
            }


            LocalDateTime starttime = searchService.changeDate(searchDTO.getStartDate());
//            LocalDateTime endTime = searchService.changeDate(searchDTO.getEndDate());

            log.info("변환값:"+starttime);
//            log.info("변환값:"+endTime);

            searchDTO.setStartDateTime(starttime);
//            searchDTO.setEndDateTime(endTime);

            log.info("최종값:"+searchDTO.getStartDateTime());
//            log.info("최종값:"+searchDTO.getEndDateTime());


            log.info("최종서치"+searchDTO.getPaymentStatus());
            log.info(searchDTO.getStoreIdx());
            log.info(searchDTO.getDistIdx());
            log.info(searchDTO.getStartDateTime());

            Page<PaymentDTO> paymentDTOS = paymentservice.distPaymentlistSearch(pageable, searchDTO, principal);
            Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);

            log.info("검색결과있을때의 DTO : "+paymentDTOS);


            model.addAllAttributes(pageinfo);
            model.addAttribute("paymentDTOS", paymentDTOS);

        }

        else {
            Page<PaymentDTO> paymentDTOS = paymentservice.distPaymentlist(pageable, principal);
            Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);

            model.addAllAttributes(pageinfo);
            model.addAttribute("paymentDTOS", paymentDTOS);
        }






        //소유 총판,매장 목록
        List<DistDTO> distDTOS = distService.distSearchforUserId(principal);
        List<StoreDTO> storeDTOS = storeService.searchStoreDistChiefId(principal);

        Long distChiefIdx = distChiefRepository.distChiefIdxSearchforUserId(principal.getName());

        log.info("아디:"+distChiefIdx);

        //소유한 전체 총판의 매출들
        Object[][] allyearlySales = paymentservice.getDistChiefYearSales(distChiefIdx);
        Object[][] allmonthSales = paymentservice.getDistChiefMonthSales(distChiefIdx);
        Object[][] alldaySales = paymentservice.getDistChiefDaySales(distChiefIdx);

        log.info("여기부터"+allyearlySales[0][1]);
        log.info(allmonthSales[0][1]);
        log.info(alldaySales[0][1]);

//        Page<PaymentDTO> paymentDTOS = paymentservice.distPaymentlist(pageable, principal);
//        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);
//
//        model.addAllAttributes(pageinfo);
        model.addAttribute("distDTOS", distDTOS);
        model.addAttribute("storeDTOS", storeDTOS);
        model.addAttribute("allyearlySales", allyearlySales);
        model.addAttribute("allmonthSales", allmonthSales);
        model.addAttribute("alldaySales", alldaySales);



        log.info("여기서시작"+startDate);
        log.info(paymentStatus);
        log.info(storeIdx);
        log.info(distIdx);
        model.addAttribute("startDate", startDate);
        model.addAttribute("paymentStatus", paymentStatus);
        model.addAttribute("storeIdx", storeIdx);
        model.addAttribute("distIdx", distIdx);

//        model.addAttribute("paymentDTOS", paymentDTOS);


        return "/admin/distchief/dist/distsales";
    }


//    @PostMapping("/admin/distchief/dist/distsales")
//    public String distSalesProc(@PageableDefault(page=1) Pageable pageable, SearchDTO searchDTO,Principal principal, Model model) throws Exception {
//
//        log.info("post 드러옴");
//
//
//        LocalDateTime starttime = searchService.changeDate(searchDTO.getStartDate());
//        LocalDateTime endTime = searchService.changeDate(searchDTO.getEndDate());
//
//        log.info("변환값:"+starttime);
//        log.info("변환값:"+endTime);
//
//        searchDTO.setStartDateTime(starttime);
//        searchDTO.setEndDateTime(endTime);
//
//        log.info("최종값:"+searchDTO.getStartDateTime());
//        log.info("최종값:"+searchDTO.getEndDateTime());
//
//
//
//        //소유 총판,매장 목록
//        List<DistDTO> distDTOS = distService.distSearchforUserId(principal);
//        List<StoreDTO> storeDTOS = storeService.searchStoreDistChiefId(principal);
//
//        Long distChiefIdx = distChiefRepository.distChiefIdxSearchforUserId(principal.getName());
//
//        log.info("아디:"+distChiefIdx);
//
//        //소유한 전체 총판의 매출들
//        Object[][] allyearlySales = paymentservice.getDistChiefYearSales(distChiefIdx);
//        Object[][] allmonthSales = paymentservice.getDistChiefMonthSales(distChiefIdx);
//        Object[][] alldaySales = paymentservice.getDistChiefDaySales(distChiefIdx);
//
//        log.info("여기부터"+allyearlySales[0][1]);
//        log.info(allmonthSales[0][1]);
//        log.info(alldaySales[0][1]);
//
//        Page<PaymentDTO> paymentDTOS = paymentservice.distPaymentlistSearch(pageable, searchDTO, principal);
//        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);
//
//        log.info("페먼트:"+paymentDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("distDTOS", distDTOS);
//        model.addAttribute("storeDTOS", storeDTOS);
//        model.addAttribute("allyearlySales", allyearlySales);
//        model.addAttribute("allmonthSales", allmonthSales);
//        model.addAttribute("alldaySales", alldaySales);
//        model.addAttribute("paymentDTOS", paymentDTOS);
//
//
//        return "/admin/distchief/dist/distsales";
//    }







}
