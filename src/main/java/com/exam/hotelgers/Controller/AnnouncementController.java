package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.service.AnnouncementService;
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

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final PageConvert pageService;

    //삽입	
    @GetMapping("/announcement/insert")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "announcement/insert";
    }

    @PostMapping("/announcement/insert")
    public String insertProc(@ModelAttribute AnnouncementDTO announcementDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(announcementService.insert(announcementDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/announcement/select";
    }

    //수정
    @GetMapping("/announcement/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        AnnouncementDTO announcementDTO = announcementService.read(id);
        model.addAttribute("announcementDTO", announcementDTO);

        if(announcementDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/announcement/select";
        }

        return "announcement/update";
    }

    @PostMapping("/announcement/update")
    public String updateProc(@ModelAttribute AnnouncementDTO announcementDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(announcementService.update(announcementDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/announcement/select";
    }

    //삭제
    @GetMapping("/announcement/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        announcementService.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/announcement/select";
    }

    //전체목록
    @GetMapping("/announcement/select")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<AnnouncementDTO> announcementDTOS = announcementService.select(pageable);


        Map<String, Integer> pageInfo = pageService.Pagination(announcementDTOS);
        model.addAllAttributes(pageInfo);

        model.addAttribute("list", announcementDTOS);

        return "announcement/select";
    }

    //개별조회(상세보기)
    @GetMapping("/announcement/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("서비스로 개별데이터 조회....");

        AnnouncementDTO announcementDTO = announcementService.read(id);
        model.addAttribute("data", announcementDTO);

        if(announcementDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/announcement/select";
        }

        return "announcement/read";
    }
}
