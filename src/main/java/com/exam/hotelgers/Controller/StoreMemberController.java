package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreMemberDTO;
import com.exam.hotelgers.service.StoreMemberService;
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
public class StoreMemberController {
    private final StoreMemberService storeMemberService;
    private final PageConvert pageService;

    //삽입	
    @GetMapping("/storemember/register")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "storemember/register";
    }

    @PostMapping("/storemember/register")
    public String insertProc(@ModelAttribute StoreMemberDTO storeMemberDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(storeMemberService.insert(storeMemberDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/storemember/list";
    }

    //수정
    @GetMapping("/storemember/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        StoreMemberDTO storeMemberDTO = storeMemberService.read(id);
        model.addAttribute("storeMemberDTO", storeMemberDTO);

        if(storeMemberDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/storemember/list";
        }

        return "storemember/update";
    }

    @PostMapping("/storemember/update")
    public String updateProc(@ModelAttribute StoreMemberDTO storeMemberDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(storeMemberService.update(storeMemberDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/storemember/list";
    }

    //삭제
    @GetMapping("/storemember/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        storeMemberService.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/storemember/list";
    }

    //전체목록
    @GetMapping("/storemember/list")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, @ModelAttribute SearchDTO searchDTO,
                             Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<StoreMemberDTO> storeMemberDTOS = storeMemberService.select(pageable, searchDTO);


         Map<String, Integer> pageInfo = pageService.Pagination(storeMemberDTOS);
         model.addAllAttributes(pageInfo);

        model.addAttribute("searchDTO", searchDTO);

        model.addAttribute("list", storeMemberDTOS);

        return "storemember/list";
    }

    //개별조회(상세보기)
    @GetMapping("/storemember/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("서비스로 개별데이터 조회....");

        StoreMemberDTO storeMemberDTO = storeMemberService.read(id);
        model.addAttribute("data", storeMemberDTO);

        if(storeMemberDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/storemember/read";
        }

        return "storemember/read";
    }
}
