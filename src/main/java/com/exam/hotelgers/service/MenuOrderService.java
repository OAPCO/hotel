package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class MenuOrderService {

    private final MenuOrderRepository menuOrderRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final SearchService searchService;
    private final MenuSheetRepository menuSheetRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;
    //등록
//    public Long register(MenuOrderDTO menuOrderDTO) {
//        // 이미 값이 채워진 MenuOrder 엔티티를 생성합니다.
//
//        log.info("들어옴@@@@@@");
//
//
//
//        log.info("들어옴22@@@@@@");
//        MenuOrder menuOrder = modelMapper.map(menuOrderDTO, MenuOrder.class);
//
//        log.info("들어옴3333@@@@@@"+menuOrder);
//
////        menuOrder.setMenuSheetList(null);
//        // MenuOrder를 저장하고 메뉴 오더의 ID를 반환합니다.
//        Long menuOrderId = menuOrderRepository.save(menuOrder).getMenuorderIdx();
//
//        log.info("들어옴4444444@@@@@@"+menuOrderId);
//
//
//
//
////        // 저장한 MenuOrder의 ID를 가져옵니다.
////        if (menuOrderId != null) {
////            // MenuSheet를 저장하기 전에 해당 MenuOrder의 ID가 있는지 확인합니다.
////            for (MenuSheetDTO menuSheetDTO : menuOrderDTO.getMenuSheetDTOList()) {
////                // MenuOrderIdx를 설정합니다.
////                menuSheetDTO.setMenuorderIdx(menuOrderId);
////
////                // MenuOrderIdx가 있는 경우에만 MenuSheet를 저장합니다.
////                if (menuSheetDTO.getMenuorderIdx() != null) {
////                    MenuSheet menuSheet = modelMapper.map(menuSheetDTO, MenuSheet.class);
////                    menuSheet.setMenuOrder(menuOrder);
////                    menuSheetRepository.save(menuSheet);
////                }
////            }
////        }
//
//        return menuOrderId;
//    }


    public Long register(MenuOrderDTO menuOrderDTO) {

        if (menuOrderDTO.getOrderState()==null){
            menuOrderDTO.setOrderState("0");
        }


        // 이미 값이 채워진 MenuOrder 엔티티를 생성합니다.
        MenuOrder menuOrder = modelMapper.map(menuOrderDTO, MenuOrder.class);
        menuOrder.setMenuSheetList(null);

        // menuOrderDTO의 roomOrderIdx로 RoomOrder를 가져옵니다.
        if (menuOrderDTO.getRoomorderIdx() != null) {
            Optional<RoomOrder> roomOrder = roomOrderRepository.findById(menuOrderDTO.getRoomorderIdx());

            if (roomOrder.isPresent()) {
                Long roomIdx = roomOrder.get().getRoomIdx();

                if (roomIdx != null) {
                    Optional<Room> room = roomRepository.findById(roomIdx);
                    room.ifPresent(menuOrder::setRoom);
                }
            }
        }

        // MenuOrder를 저장하고 메뉴 오더의 ID를 반환합니다.
        Long menuOrderId = menuOrderRepository.save(menuOrder).getMenuorderIdx();

        // 저장한 MenuOrder의 ID를 가져옵니다.
        if (menuOrderId != null) {
            // MenuSheet를 저장하기 전에 해당 MenuOrder의 ID가 있는지 확인합니다.
            for (MenuSheetDTO menuSheetDTO : menuOrderDTO.getMenuSheetDTOList()) {
                // MenuOrderIdx를 설정합니다.
                menuSheetDTO.setMenuorderIdx(menuOrderId);

                // MenuOrderIdx가 있는 경우에만 MenuSheet를 저장합니다.
                if (menuSheetDTO.getMenuorderIdx() != null) {
                    MenuSheet menuSheet = modelMapper.map(menuSheetDTO, MenuSheet.class);
                    menuSheet.setMenuOrder(menuOrder);
                    menuSheetRepository.save(menuSheet);
                }
            }
        }

        return menuOrderId;
    }







    //수정
    public void modify(MenuOrderDTO menuOrderDTO){


        Optional<MenuOrder> temp = menuOrderRepository
                .findByMenuorderIdx(menuOrderDTO.getMenuorderIdx());

        if(temp.isPresent()) {

            MenuOrder menuOrder = modelMapper.map(menuOrderDTO, MenuOrder.class);
            menuOrderRepository.save(menuOrder);
        }

    }



    //삭제
    public void delete(Long menuOrderIdx){
        menuOrderRepository.deleteById(menuOrderIdx);
    }






    public Page<MenuOrderDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "menuOrderIdx"));

        Page<MenuOrder> menuOrders = menuOrderRepository.findAll(page);
        return menuOrders.map(this::convertToDTO);
    }



    public Page<MenuOrderDTO> listByStoreIdx(Pageable pageable, Long storeIdx) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "menuOrderIdx"));

        Page<MenuOrder> menuOrders = menuOrderRepository.findByStoreIdx(storeIdx, page);
        return menuOrders.map(this::convertToDTO);
    }


    public List<MenuOrderDTO> list2(Long storeIdx) {
        List<MenuOrder> menuOrders = menuOrderRepository.findByStoreIdxMenuOrder(storeIdx);

        // Stream을 이용하여 변환 및 수집
        return menuOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private MenuOrderDTO convertToDTO(MenuOrder menuOrder) {
        MenuOrderDTO dto = modelMapper.map(menuOrder, MenuOrderDTO.class);
        dto.setMenuSheetDTOList(searchService.convertToMenuSheetList(menuOrder.getMenuSheetList()));
        return dto;
    }


    public OrderHistoryDTO getOrderHistoryByStore(Long storeIdx) {

        log.info("들어오긴 햇어요");

        List<MenuOrder> menuOrderList = menuOrderRepository.findByStoreIdxMenuOrder(storeIdx);
        List<RoomOrder> roomOrderList = roomOrderRepository.findByStoreIdxRoomOrder(storeIdx);

        log.info("menuOrderList 123"+menuOrderList);
        log.info("roomOrderList 123"+roomOrderList);

        List<MenuOrderDetailDTO> detailedMenuOrderDTOList = menuOrderList.stream().map(mo -> {
            MenuOrderDetailDTO detail = new MenuOrderDetailDTO();
            BeanUtils.copyProperties(mo, detail);

            List<MenuSheet> menuSheets = mo.getMenuSheetList();
            List<MenuSheetDTO> menuSheetDTOs = menuSheets.stream().map(sheet -> {
                MenuSheetDTO dto = new MenuSheetDTO();
                BeanUtils.copyProperties(sheet, dto);
                return dto;
            }).collect(Collectors.toList());
            detail.setMenuSheetDTOList(menuSheetDTOs);

            Store store = storeRepository.findById(mo.getStoreIdx()).orElse(null);
            if (store != null) {
                detail.setStoreName(store.getStoreName());
            }
            Room room = roomRepository.findById(mo.getRoom().getRoomIdx()).orElse(null);
            if (room != null) {
                detail.setRoomName(room.getRoomName());
            }
            Member member = memberRepository.findById(mo.getMemberIdx()).orElse(null);
            if (member != null) {
                detail.setMemberName(member.getMemberName());
            }

            return detail;
        }).collect(Collectors.toList());

        List<RoomOrderDetailDTO> detailedRoomOrderDTOList = roomOrderList.stream().map(ro -> {
            RoomOrderDetailDTO detail = new RoomOrderDetailDTO();
            BeanUtils.copyProperties(ro, detail);

            Store store = storeRepository.findById(ro.getStoreIdx()).orElse(null);
            if (store != null) {
                detail.setStoreName(store.getStoreName());
            }
            Room room = roomRepository.findById(ro.getRoomIdx()).orElse(null);
            if (room != null){
                detail.setRoomName(room.getRoomName());
            }

            return detail;
        }).collect(Collectors.toList());

        return OrderHistoryDTO.builder()
                .menuOrderDetailList(detailedMenuOrderDTOList)
                .roomOrderDetailList(detailedRoomOrderDTOList)
                .build();
    }


//    public Page<MenuOrderDTO> searchList(SearchDTO searchDTO, Pageable pageable) {
//
//        int currentPage = pageable.getPageNumber() - 1;
//        int pageCnt = 5;
//        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "menuOrderIdx"));
//
//        Page<MenuOrder> menuOrders = menuOrderRepository.multiSearch(searchDTO, page);
//        return menuOrders.map(this::convertToDTO);
//    }
//
//
//
//    public Page<MenuOrderDTO> searchMenuOrderList(String distName,String menuOrderName, String menuOrderCd, String roomCd, Pageable pageable) {
//
//        int currentPage = pageable.getPageNumber() - 1;
//        int pageCnt = 5;
//        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "menuOrderIdx"));
//
//        Page<MenuOrder> menuOrders = menuOrderRepository.menuOrderListSearch(distName, menuOrderName, menuOrderCd, roomCd, page);
//        return menuOrders.map(this::convertToDTO);
//    }






//    public List<StoreDTO> managerOfStoreList(Principal principal) {
//
//        String userId = principal.getName();
//        List<Store> stores = storeRepository.findByDistChief_DistChiefId(userId);
//
//        return dists.stream()
//                .map(dist -> modelMapper.map(dist, DistDTO.class))
//                .collect(Collectors.toList());
//    }


    public List<MenuOrderDTO> roomIdxMenuOrderSearch(Long roomIdx) {


        List<MenuOrder> menuOrders = menuOrderRepository.findByRoomorderIdx(roomIdx);


        List<MenuOrderDTO> menuOrderDTOS = menuOrders.stream()
                .map(menuOrder -> modelMapper.map(menuOrder, MenuOrderDTO.class))
                .collect(Collectors.toList());

        return menuOrderDTOS;
    }




    //단일 menuorder 객체의 메뉴시트 dto를 넣어준다
//    private MenuOrderDTO convertToDTO(MenuOrder menuOrder) {
//        MenuOrderDTO dto = modelMapper.map(menuOrder, MenuOrderDTO.class);
//        dto.setMenuSheetDTOList(searchService.convertToMenuSheetList(menuOrder.getMenuSheetList()));
//        return dto;
//    }


    //menuorder 리스트의 안의 메뉴시트 dto를 채워준다.
    private List<MenuOrderDTO> convertToDTOS(List<MenuOrder> menuOrders) {
        List<MenuOrderDTO> dtos = new ArrayList<>();

        for (MenuOrder menuOrder : menuOrders) {
            MenuOrderDTO dto = convertToDTO(menuOrder);
            dtos.add(dto);
        }
        return dtos;
    }


    public MenuOrderDTO read(Long menuorderIdx){

        Optional<MenuOrder> menuOrder = menuOrderRepository.findByMenuorderIdx(menuorderIdx);

        if (menuOrder.isPresent()) {
            MenuOrder menuOrder1 = menuOrder.get();
            return convertToDTO(menuOrder1);
        } else {
            // 예외를 던지거나 기본 값을 반환하는 등의 처리를 합니다.
            throw new NoSuchElementException("No value present for menuOrderCd: " + menuorderIdx);
        }

    }



    //roomOrder에 해당하는 메뉴 오더 목록을 반환하는데, 안의 메뉴시트가 채워진 메뉴오더리스트를 반환
    public List<MenuOrderDTO> menuOrderList(Long roomorderIdx){

        List<MenuOrder> menuOrder= menuOrderRepository.findByListMenuorderIdx(roomorderIdx);


        return convertToDTOS(menuOrder);
    }



    //menuorder에 해당하는 메뉴를 반환하는데, 안의 메뉴시트가 채워진 메뉴오더를 반환
    public MenuOrderDTO detailmenuOrderList(String menuOrderCd){

        Optional<MenuOrder> menuOrder= menuOrderRepository.findMenuSheetPayment(menuOrderCd);

        if (menuOrder.isPresent()) {
            MenuOrder menuOrder1 = menuOrder.get();
            return convertToDTO(menuOrder1);
        } else {
            // 예외를 던지거나 기본 값을 반환하는 등의 처리를 합니다.
            throw new NoSuchElementException("No value present for menuOrderCd: " + menuOrderCd);
        }
    }



}
