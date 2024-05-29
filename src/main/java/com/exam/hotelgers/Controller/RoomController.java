package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.RoomOrderRepository;
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

import java.awt.*;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
    private final MemberService memberService;
    private final MenuOrderService menuOrderService;
    private final RoomOrderRepository roomOrderRepository;
    private final MenuSheetService menuSheetService;
    private final MemberpageService memberpageService;

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

        //현재 객실이 사용중이라면
        if (roomDTO.getRoomStatus() == 2){

            //현재 객실을 사용중인 룸오더dto
            RoomOrderDTO roomOrderDTO = roomOrderService.roomOrder2Check(roomIdx);

            //현재 객실 사용중인 사용자의 정보
            MemberDTO memberDTO = memberService.findByMemberIdx(roomOrderDTO.getMemberIdx());

            //현재 사용자의 룸서비스 주문 내역
            List<MenuOrderDTO> menuOrderDTOS = menuOrderService.menuOrderList(roomOrderDTO.getRoomorderIdx());

             OrderHistoryDTO orderHistory = memberpageService.getOrderHistory(memberDTO.getMemberIdx());
                if(orderHistory != null) {
                    model.addAttribute("orderHistory", orderHistory);
                }


            //현 입실자 입실 시간 포맷 변경
            LocalDateTime dateTime = LocalDateTime.parse(roomOrderDTO.getCheckinTime());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
            String formattedCheckinTime = dateTime.format(formatter);
            roomOrderDTO.setCheckinTime(formattedCheckinTime);


            model.addAttribute("roomOrderDTO", roomOrderDTO);
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("menuOrderDTOS", menuOrderDTOS);

            log.info("화기인"+menuOrderDTOS);
            
            //메뉴오더DTO로 주문별 상세 가져오기
            for (MenuOrderDTO menu : menuOrderDTOS){
               log.info("잘가져와졋나~~" + menuOrderService.menuOrderList(menu.getMenuorderIdx()));
            }
        }


        //객실 지난 사용내역,예약 현황 (1,4)
        List<RoomOrderDTO> roomOrderDTOS = roomOrderService.RoomOrderAllSearch(searchDTO);


        log.info("지난사용내역 : "+roomOrderDTOS);

        model.addAttribute("roomDTO", roomDTO);
        model.addAttribute("roomOrderList", roomOrderDTOS);



        return "admin/manager/room/read";
    }




    @GetMapping("/window/roomregister")
    public String registerRoomWindowForm(Principal principal,Model model) {
        
        log.info("객실생성 창 컨트롤러 Get 도착");


        DistDTO distDTO = managerService.managerOfDist(principal);
        StoreDTO storeDTO = managerService.managerOfStore(principal);
        Long storeIdx = storeDTO.getStoreIdx();

        //중복 없이 객실 타입 리스트를 불러오는 쿼리문을 실행한다.
        List<RoomDTO> roomTypeList = roomService.storeroomTypeSearch(storeIdx);

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
        log.info("객실타입 화긴@@@@@@@@@@@@@ " + roomDTO.getRoomType());


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

        log.info("룸레지스터 에드 들어옴");

        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        //이 떄는 이미지 등록을 하지 않는데 mainimgname 컬럼에는 값이 있어야 편리하므로 찾아서 넣어준다~
        String roomMainImageName = roomRepository.roomTypeMainImgSearch(roomDTO.getRoomType(),roomDTO.getStoreDTO().getStoreIdx());
        roomDTO.setRoomMainimgName(roomMainImageName);


        log.info("roomMainImageName : "+ roomMainImageName);


        Long roomIdx = roomService.registeradd(roomDTO);

        log.info("roomIdx2 : "+ roomIdx);

        redirectAttributes.addFlashAttribute("result", roomIdx);

        String previousUrl = request.getHeader("referer");
        return "redirect:" + previousUrl;
    }





    @GetMapping("/admin/manager/room/listboard")
    public String listBoardForm(@PageableDefault(page = 1) Pageable pageable,Model model,Principal principal) {

        log.info("room listboard Form 도착 ");

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        List<RoomDTO> roomDTOS = managerService.managerOfLoom(principal);
        List<RoomDTO> roomTypeList = roomService.storeroomTypeSearch(storeDTO.getStoreIdx());
        List<RoomOrderDTO> roomOrderDTOS = roomOrderService.roomOrderSearch2(storeDTO.getStoreIdx());


        model.addAttribute("storeDTO",storeDTO);
        model.addAttribute("roomOrderDTOS",roomOrderDTOS);
        model.addAttribute("roomTypeList",roomTypeList);
        model.addAttribute("list", roomDTOS);
        return "admin/manager/room/listboard";
    }






    @GetMapping("/admin/manager/room/typemodify/{roomType}")
    public String read(@PathVariable String roomType, Model model,Principal principal) throws IOException {


        log.info("타입모디파이 들어옴");
        StoreDTO storeDTO = managerService.managerOfStore(principal);

        Long storeIdx = storeDTO.getStoreIdx();

        RoomDTO roomDTO = roomService.roomTypeSearchOne(storeIdx,roomType);

        SearchDTO searchDTO = new SearchDTO();

        searchDTO.setRoomType(roomType);
        searchDTO.setStoreIdx(storeDTO.getStoreIdx());

        List<ImageDTO> imageDTOS = imageService.roomTypeDetailImageSearch(searchDTO);

        log.info("roomDTO2"+roomDTO);

//        List<ImageDTO> roomDetailImgList = imageService.getRoomTypeImages(roomType,storeIdx);
//        ImageDTO roomMainImg = imageService.getRoomTypeMainImages(roomType,storeIdx);

//        log.info("로그화긴@"+roomMainImg);
//        log.info("로그화긴@"+roomMainImg.getImgName());


        model.addAttribute("roomDTO",roomDTO);
        model.addAttribute("imageDTOS",imageDTOS);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/manager/room/typeread";
    }




    @PostMapping("/room/modify")
    public String modifyProc(String roomType,
                             @RequestParam(required = false) List<MultipartFile> imgFiles,
                             @RequestParam(required = false) MultipartFile imgFile,
                             Principal principal,
                             Model model) throws IOException {


        StoreDTO storeDTO = managerService.managerOfStore(principal);


        //세부이미지 수정
        if (imgFiles!=null){
            imageService.roomImageregister(imgFiles,storeDTO.getStoreIdx(),roomType);
        }

        //메인이미지 수정
        if (imgFile!=null){
            imageService.roomMainImageregister(imgFile,storeDTO.getStoreIdx(),roomType);
        }

        

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        String referer = request.getHeader("referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/manager/room/listboard";
        }
    }





}