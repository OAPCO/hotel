package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuOrderDTO;
import com.exam.hotelgers.dto.MenuSheetDTO;
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

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class MenuOrderService {

    private final MenuOrderRepository menuOrderRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final SearchService searchService;
    private final MenuSheetRepository menuSheetRepository;




    //등록
    public Long register(MenuOrderDTO menuOrderDTO) {
        // 이미 값이 채워진 MenuOrder 엔티티를 생성합니다.
        MenuOrder menuOrder = modelMapper.map(menuOrderDTO, MenuOrder.class);
        menuOrder.setMenuSheetList(null);
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



    public MenuOrderDTO read(Long menuOrderIdx) {
        Optional<MenuOrder> menuOrderOptional = menuOrderRepository.findById(menuOrderIdx);
        if (menuOrderOptional.isPresent()) {
            MenuOrder menuOrder = menuOrderOptional.get();
            return convertToDTO(menuOrder);
        } else {
            return null;
        }
    }





    public Page<MenuOrderDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "menuOrderIdx"));

        Page<MenuOrder> menuOrders = menuOrderRepository.findAll(page);
        return menuOrders.map(this::convertToDTO);
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



    private MenuOrderDTO convertToDTO(MenuOrder menuOrder) {
        MenuOrderDTO dto = modelMapper.map(menuOrder, MenuOrderDTO.class);
        return dto;
    }





//    public List<StoreDTO> managerOfStoreList(Principal principal) {
//
//        String userId = principal.getName();
//        List<Store> stores = storeRepository.findByDistChief_DistChiefId(userId);
//
//        return dists.stream()
//                .map(dist -> modelMapper.map(dist, DistDTO.class))
//                .collect(Collectors.toList());
//    }



}
