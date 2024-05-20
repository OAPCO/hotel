package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberpageService {
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final MenuOrderRepository menuOrderRepository;
    private final MenuSheetRepository menuSheetRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final RoomRepository roomRepository;
    public List<StoreDTO> searchList(String keyword, String facilities) {
        List<Store> stores = new ArrayList<>();

        if(keyword != null && !keyword.isEmpty()) {
            // First, find all stores that match the keyword
            stores = storeRepository.findByKeyword(keyword);

            // If facilities are provided, filter the stores based on these
            if(facilities != null && !facilities.isEmpty()) {
                String[] facilitiesItems = facilities.split(",");
                List<Store> filteredStores = new ArrayList<>();
                for(String facility : facilitiesItems) {
                    filteredStores.addAll(stores.stream().filter(
                            store -> store.getFacilities().contains(facility)
                    ).collect(Collectors.toList()));
                }
                stores = filteredStores;
            }
        }
        return stores.stream()
                .distinct()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private StoreDTO convertToDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }

    public OrderHistoryDTO getOrderHistory(Long memberIdx) {
        List<MenuOrder> menuOrderList = menuOrderRepository.findByMemberIdx(memberIdx);
        List<RoomOrder> roomOrderList = roomOrderRepository.findAllByMemberIdx(memberIdx);

        List<RoomOrderDetailDTO> detailedRoomOrderDTOList = roomOrderList.stream().map(ro -> {
            RoomOrderDetailDTO detail = new RoomOrderDetailDTO();
            BeanUtils.copyProperties(ro, detail);

            Store store = storeRepository.findById(ro.getStoreIdx()).orElse(null);
            if (store != null) {
                detail.setStoreName(store.getStoreName());
            }
            Room room = roomRepository.findById(ro.getRoomIdx()).orElse(null);
            if (room !=null){
                detail.setRoomName(room.getRoomName());
            }

            return detail;
        }).collect(Collectors.toList());

        return OrderHistoryDTO.builder()
                .menuOrderList(menuOrderList)
                .roomOrderDetailList(detailedRoomOrderDTOList)
                .build();
    }
}
