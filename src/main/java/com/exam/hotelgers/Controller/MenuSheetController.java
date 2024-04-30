package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.MenuSheetDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.MenuSheetService;
import com.exam.hotelgers.service.SearchService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MenuSheetController {

    private final MenuSheetService menuSheetService;
    private final SearchService searchService;

    @GetMapping("/order/orderProgress")
    public String orderManagementForm(@PageableDefault(page = 1) Pageable pageable, Model model,
                                      @ModelAttribute("menuSheetDTO") MenuSheetDTO menuSheetDTO,
                                      @ModelAttribute("storeDTO") StoreDTO storeDTO,
                                      @ModelAttribute("roomDTO") RoomDTO roomDTO,
                                      BindingResult bindingResult) {

        log.info("order orderlistForm 도착 ");
        // 검증 결과에 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            // 오류가 있으면 로그에 기록하고 예외를 던짐
            log.error("Validation error in orderManagementForm: {}", bindingResult.getAllErrors());
            throw new IllegalArgumentException("Validation error: " + bindingResult.getAllErrors());
        }

        // MenuSheetDTO, StoreDTO, RoomDTO가 null이 아닌 경우에만 서비스 메서드 호출
        Page<MenuSheetDTO> menuSheetDTOS;
        if (menuSheetDTO != null && storeDTO != null && roomDTO != null) {
            menuSheetDTOS = menuSheetService.searchList(pageable, menuSheetDTO);
        } else {
            // null인 경우에는 빈 페이지를 반환
            menuSheetDTOS = Page.empty();
        }

        List<StoreDTO> storeList = searchService.storeList();
        List<RoomDTO> roomList = searchService.roomList();

        Map<String, Integer> pageinfo = PageConvert.Pagination(menuSheetDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeList", storeList);
        model.addAttribute("roomList", roomList);
        model.addAttribute("list", menuSheetDTOS);
        model.addAttribute("startDate", LocalDateTime.now());
        model.addAttribute("endDate", LocalDateTime.now());

        return "/order/orderProgress";
    }

    @GetMapping("/order/orderProgress/register")
    public String insertForm(Model model) {
        log.info("등록폼으로 이동....");

        // MenuSheetDTO 객체를 생성하여 모델에 추가
        model.addAttribute("menuSheetDTO", new MenuSheetDTO());
        return "order/orderProgress/register";
    }

    @PostMapping("/order/orderProgress/register")
    public String insertProc(@ModelAttribute @Valid MenuSheetDTO menuSheetDTO, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.info("서비스로 등록처리....");

        // 검증 결과에 오류가 있는지 확인
        if (bindingResult.hasErrors()) {
            // 오류가 있으면 로그에 기록하고 등록 폼으로 리다이렉트
            log.error("Validation error in insertProc: {}", bindingResult.getAllErrors());
            return "order/orderProgress/register";
        }

        // 등록 메서드 호출 후 결과에 따라 메시지 설정
        if (menuSheetService.register(menuSheetDTO) != null) {
            redirectAttributes.addFlashAttribute("processMessage", "저장하였습니다.");
        } else {
            redirectAttributes.addFlashAttribute("processMessage", "저장을 실패하였습니다.");
        }

        return "redirect:/order/orderProgress";
    }

}