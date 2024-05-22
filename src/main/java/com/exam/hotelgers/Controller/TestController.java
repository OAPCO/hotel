package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.RoomOrder;
import com.exam.hotelgers.service.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class TestController {


    private final AdminService adminService;
    private final DistService distService;
    private final SearchService searchService;
    private final StoreService storeService;
    private final RoomOrderService roomOrderService;


    @GetMapping("/test")
    public void registerForm() {

    }


//    @GetMapping("/test1")
//    public void tForm() {
//
//
//        SearchDTO searchDTO = new SearchDTO();
//
//        searchDTO.setReservationDateCheckin("2024-05-21");
//        searchDTO.setReservationDateCheckout("2024-05-30");
//        searchDTO.setRoomOrderType("특실");
//        searchDTO.setStoreIdx(1L);
//
//        //날짜변환
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime startDate = LocalDate.parse(searchDTO.getReservationDateCheckin(), formatter).atStartOfDay();
//        LocalDateTime endDate = LocalDate.parse(searchDTO.getReservationDateCheckout(), formatter).atStartOfDay();
//        log.info("변환된시작일"+startDate);
//        log.info("변환된끝일"+endDate);
//
//
//
//        searchDTO.setReservationDateCheckinDate(startDate);
//        searchDTO.setReservationDateCheckoutDate(endDate);
//
//        List<RoomOrderDTO> roomOrderDTOS = roomOrderService.roomOrderCheck(searchDTO,roomT);
//
//        log.info(roomOrderDTOS);
//
//    }



}
