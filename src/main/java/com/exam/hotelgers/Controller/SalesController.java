package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.SalesDTO;
import com.exam.hotelgers.dto.SalesDTO;
import com.exam.hotelgers.service.SalesService;
import com.exam.hotelgers.util.PageConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log
public class SalesController {
    private final SalesService salesService;
    private final PageConvert pageService;

    //삽입	
    @GetMapping("/sales/register")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "sales/register";
    }

    @PostMapping("/sales/register")
    public String insertProc(@ModelAttribute SalesDTO salesDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(salesService.insert(salesDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/sales/list";
    }

    //수정
    @GetMapping("/sales/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        SalesDTO salesDTO = salesService.read(id);
        model.addAttribute("salesDTO", salesDTO);

        if(salesDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/sales/list";
        }

        return "sales/update";
    }

    @PostMapping("/sales/update")
    public String updateProc(@ModelAttribute SalesDTO salesDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(salesService.update(salesDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/sales/list";
    }

    //삭제
    @GetMapping("/sales/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        salesService.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/sales/list";
    }

    //전체목록
    @GetMapping("/sales/list")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, @ModelAttribute SalesDTO salesDTO,
                             Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<SalesDTO> salesDTOS = salesService.select(pageable, salesDTO);


         Map<String, Integer> pageInfo = pageService.Pagination(salesDTOS);
         model.addAllAttributes(pageInfo);

        model.addAttribute("salesDTO", salesDTO);

        model.addAttribute("list", salesDTOS);

        return "sales/list";
    }

    //개별조회(상세보기)
    @GetMapping("/sales/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("서비스로 개별데이터 조회....");

        SalesDTO salesDTO = salesService.read(id);
        model.addAttribute("data", salesDTO);

        if(salesDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/sales/read";
        }

        return "sales/read";
    }
}
