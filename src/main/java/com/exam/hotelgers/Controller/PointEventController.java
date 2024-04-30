package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.dto.PointEventDTO;
import com.exam.hotelgers.service.PointEventService;
import com.exam.hotelgers.util.PageConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PointEventController {
    private final PointEventService pointEventService;
    private final PageConvert pageService;

    @GetMapping("/pointevent/insert")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        return "pointevent/insert";
    }

    @PostMapping("/pointevent/insert")
    public String insertProc(@ModelAttribute PointEventDTO pointeventDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        if(pointEventService.insert(pointeventDTO)!=null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/pointevent/select";
    }

    //수정
    @GetMapping("/pointevent/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("데이터 조회 후 수정폼으로 이동....");

        PointEventDTO pointeventDTO = pointEventService.read(id);
        model.addAttribute("pointeventDTO", pointeventDTO);

        if(pointeventDTO==null) {
            redirectAttributes.addFlashAttribute("processMessage", "자료를 읽기 실패하였습니다.");
            return "redirect:/pointevent/select";
        }

        return "pointevent/update";
    }

    @PostMapping("/pointevent/update")
    public String updateProc(@ModelAttribute PointEventDTO pointeventDTO, RedirectAttributes redirectAttributes) {
        log.info("서비스로 수정처리....");

        if(pointEventService.update(pointeventDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "수정하였습니다.");
        } else  {
            redirectAttributes.addFlashAttribute("processMessage", "수정을 실패하였습니다.");
        }

        return "redirect:/pointevent/select";
    }

    //삭제
    @GetMapping("/pointevent/delete/{id}")
    public String deleteProc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("서비스로 삭제처리....");

        pointEventService.delete(id);

        redirectAttributes.addFlashAttribute("processMessage", "삭제하였습니다.");

        return "redirect:/pointevent/select";
    }

    //전체목록
    @GetMapping("/pointevent/select")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, Model model) {
        log.info("서비스로 모든 데이터 조회....");

        Page<PointEventDTO> pointeventDTOS = pointEventService.select(pageable);


        Map<String, Integer> pageInfo = pageService.Pagination(pointeventDTOS);
        model.addAllAttributes(pageInfo);

        model.addAttribute("list", pointeventDTOS);

        return "pointevent/select";
    }

    //개별조회(상세보기)
    @GetMapping("/pointevent/{id}")
    public String readForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("서비스로 개별데이터 조회....");

        PointEventDTO pointeventDTO = pointEventService.read(id);
        model.addAttribute("data", pointeventDTO);

        if(pointeventDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/pointevent/select";
        }

        return "pointevent/read";
    }
}
