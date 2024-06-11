package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Log4j2
public class MenuSheetController {

    //    private final MenuSheetService menuSheetService;
    private final SearchService searchService;

//    @GetMapping("/order/orderProgress")
//    public String orderManagementForm(@PageableDefault(page = 1) Pageable pageable, Model model,
//                                      MenuSheetDTO menuSheetDTO,StoreDTO storeDTO,RoomDTO roomDTO,BindingResult bindingResult
//    ) {
//
//        log.info("order orderlistForm 도착 ");
//        // 검증 결과에 오류가 있는지 확인
//        if (bindingResult.hasErrors()) {
//            // 오류가 있으면 로그에 기록하고 예외를 던짐
//            log.error("Validation error in orderManagementForm: {}", bindingResult.getAllErrors());
//            throw new IllegalArgumentException("Validation error: " + bindingResult.getAllErrors());
//        }
//
//        // MenuSheetDTO, StoreDTO, RoomDTO가 null이 아닌 경우에만 서비스 메서드 호출
//        Page<MenuSheetDTO> menuSheetDTOS = null;
//        if (menuSheetDTO != null && storeDTO != null && roomDTO != null) {
//            menuSheetDTOS = menuSheetService.searchList(pageable, menuSheetDTO, storeDTO, roomDTO);
//        } else {
//            // null인 경우에는 빈 페이지를 반환
//            menuSheetDTOS = Page.empty();
//        }
//
//       menuSheetDTOS = menuSheetService.searchList(pageable, menuSheetDTO,storeDTO,roomDTO);
//
//        List<StoreDTO> storeList = searchService.storeList();
//        List<RoomDTO> roomList = searchService.roomList();
//
//
//
//
//        Map<String, Integer> pageinfo = PageConvert.Pagination(menuSheetDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("storeList",storeList);
//        model.addAttribute("roomList",roomList);
//        model.addAttribute("list", menuSheetDTOS);
//        model.addAttribute("startDate", LocalDateTime.now());
//        model.addAttribute("endDate", LocalDateTime.now());
//
//
//        return "/order/orderProgress";
//    }

}
