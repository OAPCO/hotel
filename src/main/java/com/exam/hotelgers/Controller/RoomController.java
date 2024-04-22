package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.RoomService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    private final RoomService roomService;
    private final SearchService searchService;



    @GetMapping("/admin/manager/room/register")
    public String register() {
        return "admin/manager/room/register";
    }


    @PostMapping("/admin/manager/room/register")
    public String registerProc(@Valid RoomDTO roomDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("room registerProc 도착 " + roomDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long roomIdx = roomService.register(roomDTO);


        redirectAttributes.addFlashAttribute("result", roomIdx);

        return "redirect:/admin/manager/room/list";
    }



    @GetMapping("/admin/manager/room/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model
                           ) {

        log.info("room listForm 도착 ");

        Page<RoomDTO> roomDTOS = roomService.list(pageable);

        List<StoreDTO> storeList = searchService.storeList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(roomDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeList",storeList);
        model.addAttribute("list", roomDTOS);
        return "admin/manager/room/list";
    }






    @GetMapping("/admin/manager/room/modify/{roomIdx}")
    public String modifyForm(@PathVariable Long roomIdx, Model model) {

        log.info("room modifyProc 도착 " + roomIdx);

        RoomDTO roomDTO = roomService.read(roomIdx);

        log.info("수정 전 정보" + roomDTO);
        model.addAttribute("roomDTO", roomDTO);
        return "admin/manager/room/modify";
    }


    @PostMapping("/admin/manager/room/modify")
    public String modifyProc(@Validated RoomDTO roomDTO,
                             BindingResult bindingResult, Model model) {

        log.info("room modifyProc 도착 " + roomDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/manager/room/modify";
        }


        roomService.modify(roomDTO);

        log.info("업데이트 이후 정보 " + roomDTO);

        return "redirect:/admin/manager/room/list";
    }

    @GetMapping("/admin/manager/room/delete/{roomIdx}")
    public String deleteProc(@PathVariable Long roomIdx) {

        roomService.delete(roomIdx);

        return "redirect:/admin/manager/room/list";
    }

    @GetMapping("/admin/manager/room/{roomIdx}")
    public String readForm(@PathVariable Long roomIdx, Model model) {


        RoomDTO roomDTO = roomService.read(roomIdx);
        model.addAttribute("roomDTO", roomDTO);



        if(roomDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:admin/manager/room/list";
        }

        return "admin/manager/room/orderlist";
    }

}
