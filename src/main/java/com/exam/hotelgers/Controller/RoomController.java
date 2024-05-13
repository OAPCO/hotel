package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    private final RoomService roomService;
    private final ManagerService managerService;
    private final SearchService searchService;
    private final HttpServletRequest request;
    private final RoomOrderService roomOrderService;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;


    @GetMapping("/admin/manager/room/register")
    public String register() {
        return "admin/manager/room/register";
    }


    @PostMapping("/admin/manager/room/register")
    public String registerProc(@Valid RoomDTO roomDTO,List<ImageDTO> imageDTOs,
                               BindingResult bindingResult,
                               @RequestParam(required = false)MultipartFile imgFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        log.info("room registerProc 도착 " + roomDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long roomIdx = roomService.register(roomDTO, imgFile);


        redirectAttributes.addFlashAttribute("result", roomIdx);

//        String previousUrl = request.getHeader("referer");
//        return "redirect:" + previousUrl;

        return "admin/manager/room/list";
    }



    @GetMapping("/admin/manager/room/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model,Principal principal
                           ) {

        log.info("room listForm 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        Page<RoomDTO> roomDTOS = managerService.managerOfLoom(principal,pageable);


        Map<String, Integer> pageinfo = PageConvert.Pagination(roomDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeDTO",storeDTO);
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
                             @RequestParam(required = false)MultipartFile imgFile,
                             BindingResult bindingResult, Model model) throws IOException {

        log.info("room modifyProc 도착 " + roomDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/manager/room/modify";
        }


        roomService.modify(roomDTO, imgFile );

        log.info("업데이트 이후 정보 " + roomDTO);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }

    @GetMapping("/admin/manager/room/delete/{roomIdx}")
    public String deleteProc(@PathVariable Long roomIdx) throws IOException {

        roomService.delete(roomIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }



    @GetMapping("/admin/manager/room/{roomIdx}")
    public String readForm(@PathVariable Long roomIdx, Model model, SearchDTO searchDTO, @PageableDefault(page = 1) Pageable pageable) {


        RoomDTO roomDTO = roomService.read(roomIdx);

        Page<RoomOrderDTO> roomOrderDTOS = roomOrderService.endRoomOrderSearch(pageable,searchDTO);
        RoomOrderDTO roomOrderDTO = roomOrderService.roomOrderStatusCheck(searchDTO);
        MemberDTO memberDTO = roomOrderService.roomOrderMemberCheck(searchDTO);

        model.addAttribute("roomDTO", roomDTO);
        model.addAttribute("roomOrderList", roomOrderDTOS);
        model.addAttribute("roomOrderDTO", roomOrderDTO);
        model.addAttribute("memberDTO", memberDTO);


        return "admin/manager/room/read";
    }




    @GetMapping("/window/roomregister")
    public String registerRoomWindowForm(Principal principal,Model model) {
        
        log.info("객실생성 창 컨트롤러 Get 도착");

        StoreDTO storeDTO = managerService.managerOfStore(principal);
        DistDTO distDTO = managerService.managerOfDist(principal);

        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("distDTO",distDTO);



        return "window/roomregister";
    }


    @PostMapping("/window/roomregister")
    public String registerRoomWindowProc(@Valid RoomDTO roomDTO,
                               BindingResult bindingResult,
                               @RequestParam(required = false)MultipartFile imgFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        log.info("객실생성 창 컨트롤러 Post 도착" + roomDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long roomIdx = roomService.register(roomDTO, imgFile);


        redirectAttributes.addFlashAttribute("result", roomIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }





    @GetMapping("/admin/manager/room/listboard")
    public String listBoardForm(@PageableDefault(page = 1) Pageable pageable, Model model,Principal principal
    ) {

        log.info("room listboard Form 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        Page<RoomDTO> roomDTOS = managerService.managerOfLoom(principal,pageable);
        Page<RoomOrderDTO> roomOrderList = roomOrderService.roomOrderSearch(pageable,storeDTO.getStoreIdx());



        if (roomOrderList == null){
            log.info("갑이없는데용");
        }
        for (RoomOrderDTO room : roomOrderList) {
            log.info("예약필요한거@@@@@@@@@@@@@@ " + room.getRoomorderIdx());
        }



        Map<String, Integer> pageinfo = PageConvert.Pagination(roomDTOS);
        Map<String, Integer> pageinfo2 = PageConvert.Pagination(roomOrderList);

        model.addAllAttributes(pageinfo);
        model.addAllAttributes(pageinfo2);
        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("roomOrderList",roomOrderList);
        model.addAttribute("list", roomDTOS);
        return "admin/manager/room/listboard";
    }






}