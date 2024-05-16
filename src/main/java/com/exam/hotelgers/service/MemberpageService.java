package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuOrderDTO;
import com.exam.hotelgers.dto.MenuSheetDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.MenuOrder;
import com.exam.hotelgers.entity.MenuSheet;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.MenuOrderRepository;
import com.exam.hotelgers.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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


}
