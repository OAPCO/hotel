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
@RequestMapping("/manager")
public class RoomController {

    private final RoomService roomService;
    private final SearchService searchService;



    @GetMapping("/room/register")
    public String register() {
        return "manager/room/register";
    }


    @PostMapping("/room/register")
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

        return "redirect:/manager/room/list";
    }


//    @PostMapping("/room/list")
//    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
//                           @RequestParam(value="distName", required = false) String distName,
//                           @RequestParam(value="branchName", required = false) String branchName,
//                           @RequestParam(value="roomName", required = false) String roomName,
//                           @RequestParam(value="roomPType", required = false) StorePType roomPType,
//                           @RequestParam(value="roomStatus", required = false) StoreStatus roomStatus
//    ){
//
//
//        log.info("들어온 총판 @@@@@ + " + distName);
//        log.info("들어온 상태 값 : @@ + " + roomStatus);
//        log.info("들어온 피타입 값 : @@ + " + roomPType);
//
//
//
//        Page<RoomDTO> roomDTOS = roomService.searchList(distName,branchName,roomName,roomPType,roomStatus,pageable);
//
//
//
//
//        List<DistDTO> distList = searchService.distList();
//        List<BranchDTO> branchList = searchService.branchList();
//        List<StoreDTO> roomList = searchService.roomList();
//
//
//        Map<String, Integer> pageinfo = PageConvert.Pagination(roomDTOS);
//
//        model.addAllAttributes(pageinfo);
//        model.addAttribute("distList",distList);
//        model.addAttribute("branchList",branchList);
//        model.addAttribute("roomList",roomList);
//        model.addAttribute("list", roomDTOS);
//        return "manager/room/list";
//    }

    @GetMapping("/room/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model
                           ) {

        log.info("room listForm 도착 ");

        Page<RoomDTO> roomDTOS = roomService.list(pageable);

        List<StoreDTO> storeList = searchService.storeList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(roomDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeList",storeList);
        model.addAttribute("list", roomDTOS);
        return "manager/room/list";
    }






    @GetMapping("/room/modify/{roomIdx}")
    public String modifyForm(@PathVariable Long roomIdx, Model model) {

        log.info("room modifyProc 도착 " + roomIdx);

        RoomDTO roomDTO = roomService.read(roomIdx);

        log.info("수정 전 정보" + roomDTO);
        model.addAttribute("roomDTO", roomDTO);
        return "manager/room/modify";
    }


    @PostMapping("/room/modify")
    public String modifyProc(@Validated RoomDTO roomDTO,
                             BindingResult bindingResult, Model model) {

        log.info("room modifyProc 도착 " + roomDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "manager/room/modify";
        }


        roomService.modify(roomDTO);

        log.info("업데이트 이후 정보 " + roomDTO);

        return "redirect:/manager/room/list";
    }

    @GetMapping("/room/delete/{roomIdx}")
    public String deleteProc(@PathVariable Long roomIdx) {

        roomService.delete(roomIdx);

        return "redirect:/manager/room/list";
    }

    @GetMapping("/room/{roomIdx}")
    public String readForm(@PathVariable Long roomIdx, Model model) {


        RoomDTO roomDTO = roomService.read(roomIdx);
        model.addAttribute("roomDTO", roomDTO);



        if(roomDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:manager/room/list";
        }

        return "manager/room/orderlist";
    }

}
