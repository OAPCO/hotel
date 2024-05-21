package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.service.*;
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

@Controller
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    private final RoomService roomService;
    private final ManagerService managerService;
    private final SearchService searchService;
    private final HttpServletRequest request;
    private final RoomOrderService roomOrderService;
    private final RoomRepository roomRepository;
    private final ImageService imageService;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;





    @GetMapping("/admin/manager/room/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model,Principal principal
                           ) {

        log.info("room listForm 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        List<RoomDTO> roomDTOS = managerService.managerOfLoom(principal);


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


        DistDTO distDTO = managerService.managerOfDist(principal);
        StoreDTO storeDTO = managerService.managerOfStore(principal);
        Long storeIdx = storeDTO.getStoreIdx();

        //중복 없이 객실 타입 리스트를 불러오는 쿼리문을 실행한다.
        List<RoomDTO> roomTypeList = roomService.roomTypeSearch(storeIdx);

        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("distDTO",distDTO);
        model.addAttribute("roomTypeList",roomTypeList);



        return "window/roomregister";
    }


    @PostMapping("/window/roomregister")
    public String registerRoomWindowProc(@Valid RoomDTO roomDTO, BindingResult bindingResult,
                                         @RequestParam("imgFile") List<MultipartFile> imgFile,
                                         @RequestParam("mainimgFile") MultipartFile mainimgFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        log.info("객실생성 창 컨트롤러 Post 도착" + roomDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long roomIdx = roomService.register(roomDTO, imgFile, mainimgFile);


        redirectAttributes.addFlashAttribute("result", roomIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }



    //이것은 file 없이 객실을 생성하는 곳이다. 하하 (기존의 객실타입으로 생성할 때)
    @PostMapping("/window/roomregisteradd")
    public String registerRoomWindowaddProc(@Valid RoomDTO roomDTO, BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) throws IOException {


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        //이 떄는 이미지 등록을 하지 않는데 mainimgname 컬럼에는 값이 있어야 편리하므로 찾아서 넣어준다~
        String roomMainImageName = roomRepository.roomTypeMainImgSearch(roomDTO.getRoomType(),roomDTO.getStoreDTO().getStoreIdx());
        roomDTO.setRoomMainimgName(roomMainImageName);


        Long roomIdx = roomService.registeradd(roomDTO);

        redirectAttributes.addFlashAttribute("result", roomIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }





    @GetMapping("/admin/manager/room/listboard")
    public String listBoardForm(@PageableDefault(page = 1) Pageable pageable,Model model,Principal principal) {

        log.info("room listboard Form 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        List<RoomDTO> roomDTOS = managerService.managerOfLoom(principal);
        List<RoomDTO> roomTypeList = roomService.roomTypeSearch(storeDTO.getStoreIdx());
        List<RoomOrderDTO> roomOrderDTOS = roomOrderService.roomOrderSearch2(storeDTO.getStoreIdx());


        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("roomOrderDTOS",roomOrderDTOS);
        model.addAttribute("roomTypeList",roomTypeList);
        model.addAttribute("list", roomDTOS);
        return "admin/manager/room/listboard";
    }






    @GetMapping("/admin/manager/room/typemodify/{roomType}")
    public String read(@PathVariable String roomType, Model model,Principal principal) throws IOException {

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        Long storeIdx = storeDTO.getStoreIdx();

        RoomDTO roomDTO = roomService.roomTypeSearchOne(storeIdx,roomType);



        List<ImageDTO> roomDetailImgList = imageService.getRoomTypeImages(roomType,storeIdx);



        model.addAttribute("roomDTO",roomDTO);
        model.addAttribute("roomDetailImgList",roomDetailImgList);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/manager/room/typeread";
    }




//    @PostMapping("/banner/modify")
//    public String modifyProc(@RequestParam Long bannerIdx,
//                             @RequestParam("imgFile") List<MultipartFile> imgFile,
//                             Model model) throws IOException {
//
//
//        imageService.bannerImageregister(imgFile,bannerIdx);
//
//        model.addAttribute("bucket", bucket);
//        model.addAttribute("region", region);
//        model.addAttribute("folder", folder);
//
//        String referer = request.getHeader("referer");
//
//        if (referer != null && !referer.isEmpty()) {
//            return "redirect:" + referer;
//        } else {
//            return "redirect:/admin/admin/banner/list";
//        }
//    }





}