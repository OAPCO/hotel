package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class MenuSheetService {

    private final MenuSheetRepository menuSheetRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final OrderRepository orderRepository;

    // 등록
    public Long register(MenuSheetDTO menuSheetDTO) {
        // StoreDTO, RoomDTO, OrderDTO 가져오기
        StoreDTO storeDTO = menuSheetDTO.getStoreDTO();
        RoomDTO roomDTO = menuSheetDTO.getRoomDTO();
        OrderDTO orderDTO = menuSheetDTO.getOrderDTO();

        // Null 체크
        if (storeDTO == null || roomDTO == null || orderDTO == null) {
            throw new IllegalArgumentException("StoreDTO, RoomDTO, or OrderDTO is null in MenuSheetDTO");
        }

        // StoreDTO로부터 Store 엔티티 찾기
        Optional<Store> storeOptional = storeRepository.findByStoreCd(storeDTO.getStoreCd());
        Store store = storeOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 매장 코드입니다."));

        // RoomDTO로부터 Room 엔티티 찾기
        Optional<Room> roomOptional = roomRepository.findByRoomCd(roomDTO.getRoomCd());
        Room room = roomOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 룸 코드입니다."));

        // OrderDTO로부터 Order 엔티티 찾기
        Optional<Order> orderOptional = orderRepository.findByOrderCd(orderDTO.getOrderCd());
        Order order = orderOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 주문 코드입니다."));

        // MenuSheetDTO를 MenuSheet 엔티티로 변환
        MenuSheet menuSheet = modelMapper.map(menuSheetDTO, MenuSheet.class);
        menuSheet.setStore(store);
        menuSheet.setRoom(room);
        menuSheet.setOrder(order);

        // MenuSheet 엔티티 저장 후 ID 반환
        return menuSheetRepository.save(menuSheet).getMenuSheetIdx();
    }

    public void modify(MenuSheetDTO menuSheetDTO) {
        // MenuSheetDTO에서 MenuSheet 엔티티를 찾기 위해 MenuSheetIdx 가져오기
        Long menuSheetIdx = menuSheetDTO.getMenuSheetIdx();
        Optional<MenuSheet> menuSheetOptional = menuSheetRepository.findByMenuSheetIdx(menuSheetIdx);
        menuSheetOptional.ifPresent(menuSheet -> {
            // MenuSheetDTO의 내용으로 MenuSheet 엔티티 수정
            modelMapper.map(menuSheetDTO, menuSheet);
            menuSheetRepository.save(menuSheet);
        });
    }

    public Page<MenuSheetDTO> searchList(Pageable pageable, MenuSheetDTO menuSheetDTO) {
        // 검색 조건 추출
        StoreDTO storeDTO = menuSheetDTO.getStoreDTO();
        RoomDTO roomDTO = menuSheetDTO.getRoomDTO();
        String storeName = (storeDTO != null) ? storeDTO.getStoreName() : null;
        String roomCd = (roomDTO != null) ? roomDTO.getRoomCd() : null;

        // MenuSheetRepository에서 검색 메서드 호출
        Page<MenuSheet> menuSheetPage = menuSheetRepository.menuSheetListSearch(
                storeName,
                roomCd,
                menuSheetDTO.getNewOrderNo(),
                menuSheetDTO.getMenuSheetState(),
                menuSheetDTO.getStartDate(),
                menuSheetDTO.getEndDate(),
                menuSheetDTO.getOrderProgressStatus(),
                menuSheetDTO.getMenuSheetName(),
                pageable
        );

        // 검색 결과를 DTO로 변환하여 반환
        return menuSheetPage.map(menuSheet -> modelMapper.map(menuSheet, MenuSheetDTO.class));
    }
}
