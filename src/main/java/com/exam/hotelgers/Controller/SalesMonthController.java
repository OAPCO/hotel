package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.SalesMonthDTO;
import com.exam.hotelgers.service.SalesMonthService;
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
public class SalesMonthController {
    private final SalesMonthService salesMonthService;
    private final PageConvert pageService;

    //삽입	
    @GetMapping("/salesmonth/register")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "salesmonth/register";
    }

    @PostMapping("/salesmonth/register")
    public String insertProc(@ModelAttribute SalesMonthDTO salesMonthDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(salesMonthService.insert(salesMonthDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/salesmonth/list";
    }

    //수정
    @GetMapping("/salesmonth/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        SalesMonthDTO salesMonthDTO = salesMonthService.read(id);
        model.addAttribute("salesMonthDTO", salesMonthDTO);

        if(salesMonthDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/salesmonth/list";
        }

        return "salesmonth/update";
    }

    @PostMapping("/salesmonth/update")
    public String updateProc(@ModelAttribute SalesMonthDTO salesMonthDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(salesMonthService.update(salesMonthDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/salesmonth/list";
    }

    //삭제
    @GetMapping("/salesmonth/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        salesMonthService.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/salesmonth/list";
    }

    //전체목록
    @GetMapping("/salesmonth/list")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, @ModelAttribute SalesMonthDTO salesMonthDTO,
                             Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<SalesMonthDTO> salesMonthDTOS = salesMonthService.select(pageable, salesMonthDTO);


         Map<String, Integer> pageInfo = pageService.Pagination(salesMonthDTOS);
         model.addAllAttributes(pageInfo);

        model.addAttribute("salesMonthDTO", salesMonthDTO);

        model.addAttribute("list", salesMonthDTOS);

        return "salesmonth/list";
    }

    //개별조회(상세보기)
    @GetMapping("/salesmonth/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("서비스로 개별데이터 조회....");

        SalesMonthDTO salesMonthDTO = salesMonthService.read(id);
        model.addAttribute("data", salesMonthDTO);

        if(salesMonthDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/salesmonth/read";
        }

        return "salesmonth/read";
    }
}
