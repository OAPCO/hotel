package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class MenuSheetService {

    private final MenuSheetRepository menuSheetRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final MenuOrderRepository menuOrderRepository;









//    public Page<MenuSheetDTO> searchList(Pageable pageable, MenuSheetDTO menuSheetDTO, StoreDTO storeDTO, RoomDTO roomDTO) {
//
//        int currentPage = pageable.getPageNumber() - 1;
//        int pageCnt = 5;
//        Pageable page = PageRequest.of(currentPage, pageCnt,
//                Sort.by(Sort.Direction.DESC,"menuSheetIdx"));
//        String storeName = null;
//        String roomCd = null;
//
//        // 매장 DTO가 null이 아닌 경우에 매장명을 가져옴
//        if (menuSheetDTO.getStoreDTO() != null) {
//            storeName = menuSheetDTO.getStoreDTO().getStoreName();
//        }
//
//        // 룸 DTO가 null이 아닌 경우에 룸 코드를 가져옴
//        if (menuSheetDTO.getRoomDTO() != null) {
//            roomCd = menuSheetDTO.getRoomDTO().getRoomCd();
//        }
//
//
//        Page<MenuSheet> menuSheets = menuSheetRepository.menuSheetListSearch(
//                storeName,  // 수정: storeName 변수로 변경
//                roomCd, // 수정: roomCd 변수로 변경
//                menuSheetDTO.getNewOrderNo(), //신규 주문번호
//                menuSheetDTO.getMenuSheetState(),//주문서 상태 0.주문전, 1.조리요청, 2.결제요청, 3.결제완료, 4.결제취소, 5.조리완료, 6.배달요청, 7.배달완료
//                menuSheetDTO.getStartDate(),//시작날짜
//                menuSheetDTO.getEndDate(),//종료날짜
//                menuSheetDTO.getOrderProgressStatus(),//주문상태(NEW 신규,CHECK 접수,CANCEL 취소,CALL 호출,CLOSE 완료)
//                menuSheetDTO.getMenuSheetName(),//주문서 이름
//                pageable);
//
//
//        Page<MenuSheetDTO> result = menuSheets.map(data->modelMapper.map(data,MenuSheetDTO.class));
//
//        return result;
//    }



    //메뉴
    public List<MenuSheetDTO> menuSearch(Long menuOrderIdx) {


        List<MenuSheet> menuSheetList = menuSheetRepository.findByMenuOrderMenu(menuOrderIdx);


        List<MenuSheetDTO> MenuSheetDTOs = menuSheetList.stream()
                .map(menuSheet -> modelMapper.map(menuSheet, MenuSheetDTO.class))
                .collect(Collectors.toList());

        return MenuSheetDTOs;
    }



//    public List<MenuSheetDTO> findMenuSheetPayment(String menuOrderCd) {
//
//
//        List<MenuSheet> menuSheetList = menuSheetRepository.findMenuSheetPayment(menuOrderCd);
//
//
//        List<MenuSheetDTO> MenuSheetDTOs = menuSheetList.stream()
//                .map(menuSheet -> modelMapper.map(menuSheet, MenuSheetDTO.class))
//                .collect(Collectors.toList());
//
//        return MenuSheetDTOs;
//    }
}
