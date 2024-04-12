package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.BranchRepository;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BranchRepository branchRepository;
    private final BrandRepository brandRepository;
    private final DistService distService;
    private final BranchService branchService;
    private final StoreService storeService;


    public List<DistDTO> distList() {
        List<Dist> dists = distRepository.findAll();
        return dists.stream()
                .map(dist -> modelMapper.map(dist, DistDTO.class))
                .collect(Collectors.toList());
    }


    public List<BranchDTO> branchList() {
        List<Branch> branchs = branchRepository.findAll();
        return branchs.stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .collect(Collectors.toList());
    }


    public List<BrandDTO> brandList() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }




//    public List<SearchDTO> searchStoreList(Pageable pageable,SearchDTO searchDTO) {
//
//        int currentPage = pageable.getPageNumber() - 1;
//        int pageCnt = 5;
//        pageable = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));
//
//
//        Page<StoreDTO> storeList = storeService.list(pageable);
//        Optional<Dist> dist = distRepository.findByDistName(searchDTO.getDistName());
//        // 다른 서비스를 이용하여 정보를 가져옴...
//
//        // 가져온 정보들을 조합하여 검색 결과를 생성
////        for (StoreDTO store : storeList) {
////            for (DistDTO dist : dist) {
////                if (store.getStoreIdx().equals(storeIdx) && dist.getDistIdx().equals(distIdx)) {
////                    SearchResultDTO result = new SearchResultDTO();
////                    result.setStoreDTO(store);
////                    result.setDistDTO(dist);
////                    // 필요한 대로 다른 정보도 추가...
////
////                    searchResults.add(result);
////                }
////            }
////        }
//
//        return stores.map(this::convertToDTO);
//    }



}
