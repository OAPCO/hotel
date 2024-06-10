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
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final SearchService searchService;
    private final MenuSheetRepository menuSheetRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;



    public Long register(MenuOrderDTO menuOrderDTO) {


        menuOrderDTO.setOrderState("3");


        log.info("스테화긴"+menuOrderDTO.getOrderState());

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





    @Transactional
    public void menuOrderStatusChange(Long menuorderIdx, String orderStatus) {
        menuOrderRepository.menuOrderStatusChange(menuorderIdx,orderStatus);
    }

    @Transactional
    public void menuOrderPaymentCheck(Long menuorderIdx) {
        menuOrderRepository.menuOrderPaymentCheck(menuorderIdx);
    }


}
