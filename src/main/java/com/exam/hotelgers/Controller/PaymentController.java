package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.service.PaymentService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PaymentController {

    private final PaymentService paymentservice;
    private final PageConvert pageService;

    @GetMapping("/payment/register")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "payment/register";
    }


    @PostMapping("/payment/register")
    public String insertProc(@ModelAttribute PaymentDTO paymentDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(paymentservice.insert(paymentDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/payment/list";
    }


    @GetMapping("/payment/list")
    public String list(@PageableDefault(page=1) Pageable pageable, @ModelAttribute PaymentDTO paymentDTO,
                             Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<PaymentDTO> paymentDTOS = paymentservice.list(pageable);

        Map<String, Integer> pageInfo = pageService.Pagination(paymentDTOS);
        model.addAllAttributes(pageInfo);

        model.addAttribute("list", paymentDTOS);

        return "payment/list";
    }




    @GetMapping("/payment/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        PaymentDTO paymentDTO = paymentservice.read(id);
        model.addAttribute("paymentDTO", paymentDTO);

        if(paymentDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/payment/list";
        }

        return "payment/update";
    }


    @PostMapping("/payment/update")
    public String updateProc(@ModelAttribute PaymentDTO paymentDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(paymentservice.update(paymentDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/payment/list";
    }

    @GetMapping("/payment/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        paymentservice.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/payment/list";
    }

    @GetMapping("/payment/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,@PageableDefault(page=1) Pageable pageable) {
        log.info("서비스로 개별데이터 조회....");

        PaymentDTO paymentDTO = paymentservice.read(id);
        model.addAttribute("data", paymentDTO);

        if(paymentDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/payment/read";
        }
        Page<PaymentDTO> paymentDTOS = paymentservice.list(pageable);

        Map<String, Integer> pageInfo = pageService.Pagination(paymentDTOS);
        model.addAllAttributes(pageInfo);

        model.addAttribute("list", paymentDTOS);
        return "payment/read";
    }
}
