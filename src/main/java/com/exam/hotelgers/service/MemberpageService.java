package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Store;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberpageService {
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    public List<StoreDTO> searchList(String keyword) {
        List<Store> stores = storeRepository.findByKeyword(keyword);
        return stores.stream()
                .map(this::convertToDTO) // this::convertToDTO is assumed to be a method which converts Store to StoreDTO
                .collect(Collectors.toList());
    }
    private StoreDTO convertToDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }
}
