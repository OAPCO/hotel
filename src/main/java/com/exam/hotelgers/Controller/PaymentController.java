package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.PaymentService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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


//    @GetMapping("/payment/list")
//    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {
//
//        log.info("payment listForm 도착 ");
//
//        Page<PaymentDTO> paymentDTOS = paymentservice.list(pageable);
//
//        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("list", paymentDTOS);
//        return "payment/list";
//    }




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



    @GetMapping("/admin/distchief/dist/distsales")
    public String distSalesForm(@PageableDefault(page=1) Pageable pageable, Principal principal, Model model) throws Exception {

        
        //소유 총판 목록
        List<DistDTO> distDTOS = distService.distSearchforUserId(principal);

        Long distChiefIdx = distChiefRepository.distChiefIdxSearchforUserId(principal.getName());

        log.info("아디:"+distChiefIdx);

        //소유한 전체 총판의 매출들
        Object[][] allyearlySales = paymentservice.getDistChiefYearSales(distChiefIdx);
        Object[][] allmonthSales = paymentservice.getDistChiefMonthSales(distChiefIdx);
        Object[][] alldaySales = paymentservice.getDistChiefDaySales(distChiefIdx);

        log.info("여기부터"+allyearlySales[0][1]);
        log.info(allmonthSales[0][1]);
        log.info(alldaySales[0][1]);


//        //이 부분은 일단 모든 총판의 매출 목록을 가져오는걸로 간다.
//        Page<PaymentDTO> paymentDTOS = paymentservice.list(pageable, storeDTO.getStoreIdx());


        model.addAttribute("distDTOS", distDTOS);
        model.addAttribute("allyearlySales", allyearlySales);
        model.addAttribute("allmonthSales", allmonthSales);
        model.addAttribute("alldaySales", alldaySales);


        return "/admin/distchief/dist/distsales";
    }

}
