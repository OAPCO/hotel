package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.MenuCateRepository;
import com.exam.hotelgers.repository.StoreRepository;
import com.exam.hotelgers.service.QrRoomService;
import com.exam.hotelgers.service.RoomService;
import com.exam.hotelgers.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class QrRoomController {
    private final RoomService roomService;
    private final QrRoomService qrRoomService;
    private final StoreRepository storeRepository;
    private final MenuCateRepository menuCateRepository;
    private final ModelMapper modelMapper;
    private final SearchService searchService;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;

    @GetMapping("/member/memberpage/newindex")
    public String newindex() {

        return "member/memberpage/newindex";
    }

    @GetMapping("/member/memberpage/scroll")
    public String scroll() {

        return "member/memberpage/scroll";
    }
    @GetMapping("/member/memberpage/qrroot/{roomIdx}")
    public String getRoomDetails(@PathVariable Long roomIdx, Model model) {
        RoomDTO roomDTO = roomService.read(roomIdx);

        if (roomDTO != null && roomDTO.getStoreDTO() != null) {
            Store store = storeRepository.findById(roomDTO.getStoreDTO().getStoreIdx())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found"));

            List<MenuCateDTO> menuCateList = store.getMenuCateList().stream()
                    .map(menuCate -> modelMapper.map(menuCate, MenuCateDTO.class))
                    .collect(Collectors.toList());

            for (MenuCateDTO menuCateDTO : menuCateList) {
                List<DetailmenuDTO> detailMenuList = menuCateDTO.getDetailMenuDTOList().stream()
                        .map(detailMenu -> modelMapper.map(detailMenu, DetailmenuDTO.class))
                        .collect(Collectors.toList());
                menuCateDTO.setDetailMenuDTOList(detailMenuList);
            }

            StoreDTO storeDTO = modelMapper.map(store, StoreDTO.class);

            model.addAttribute("room", roomDTO);
            model.addAttribute("store", storeDTO);
            model.addAttribute("menuCateList", menuCateList);
        } else {
            throw new IllegalArgumentException("Room or Store not found");
        }

        return "/member/memberpage/qrroot"; // 이 페이지 이름은 실제 프로젝트에 맞게 변경해야 함
    }
}
