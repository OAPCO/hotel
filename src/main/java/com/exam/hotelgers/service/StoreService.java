package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.BranchRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BranchRepository branchRepository;
    private final BrandRepository brandRepository;
    private final SearchService searchService;


    public Long register(StoreDTO storeDTO) {


        Optional<Dist> dist = distRepository.findByDistCd(storeDTO.getDistDTO().getDistCd());
        Optional<Branch> branch = branchRepository.findByBranchCd(storeDTO.getBranchDTO().getBranchCd());
        Optional<Brand> brand = brandRepository.findByBrandCd(storeDTO.getBrandDTO().getBrandCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }
        if (!branch.isPresent()) {
            throw new IllegalStateException("존재하지 않는 지사 코드입니다.");
        }
        if (!brand.isPresent()) {
            throw new IllegalStateException("존재하지 않는 브랜드 코드입니다.");
        }





        Optional<Store> temp = storeRepository
                .findByStoreCd(storeDTO.getStoreCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }




        Store store = modelMapper.map(storeDTO, Store.class);


        if (storeDTO.getStorePType().equals("DIRECTSTORE")){
            store.setStorePType(StorePType.DIRECTSTORE);
        }
        if (storeDTO.getStorePType().equals("FRANCHISEE")){
            store.setStorePType(StorePType.FRANCHISEE);
        }

        if (storeDTO.getStoreStatus().equals("ON")){
            store.setStoreStatus(StoreStatus.ON);
        }
        if (storeDTO.getStoreStatus().equals("OFF")){
            store.setStoreStatus(StoreStatus.OFF);
        }


        switch (storeDTO.getStoreGrade()){
            case ONE : store.setStoreGrade(StoreGrade.ONE);
            break;
            case TWO : store.setStoreGrade(StoreGrade.TWO);
            break;
            case THREE : store.setStoreGrade(StoreGrade.THREE);
            break;
            case FOUR : store.setStoreGrade(StoreGrade.FOUR);
            break;
            case FIVE : store.setStoreGrade(StoreGrade.FIVE);
            break;
        }


        store.setDist(dist.get());
        store.setBranch(branch.get());
        store.setBrand(brand.get());



        storeRepository.save(store);

        return storeRepository.save(store).getStoreIdx();
    }



    public void modify(StoreDTO storeDTO){


        Optional<Store> temp = storeRepository
                .findByStoreIdx(storeDTO.getStoreIdx());

        if(temp.isPresent()) {

            Store store = modelMapper.map(storeDTO, Store.class);
            storeRepository.save(store);
        }

    }




    //이 메소드는 store가 참조하는 테이블(dist,branch,brand)과 store를 참조하는 테이블 Order,room도 함께 조회합니다.
    //store를 참조하는 room,order의 dto는 여러개가 있을 수 있으므로 DTO에 List로 선언되어 있습니다.
    public StoreDTO read(Long storeIdx) {
        Optional<Store> optionalStore = storeRepository.findById(storeIdx);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            StoreDTO dto = modelMapper.map(store, StoreDTO.class);
            dto.setOrderDTOList(searchService.convertToOrderDTOList(store.getOrderList())); //orderDTOList
            dto.setRoomDTOList(searchService.convertToRoomDTOList(store.getRoomList())); //roomDTOList
            dto.setDistDTO(searchService.convertToDistDTO(store.getDist()));
            dto.setBranchDTO(searchService.convertToBranchDTO(store.getBranch()));
            dto.setBrandDTO(searchService.convertToBrandDTO(store.getBrand()));

            return dto;
        } else {
            return null;
        }
    }



    private List<OrderDTO> convertOrderToDTOs(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }





    public Page<StoreDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));

        Page<Store> stores = storeRepository.findAll(page);
        return stores.map(this::convertToDTO);
    }



    public Page<StoreDTO> searchList(String distName,String branchName,String storeName,StoreGrade storeGrade,String storeCd,String storeChiefEmail,String storeChief,
                                     String brandName,StoreStatus storeStatus,StorePType storePType, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));

        Page<Store> stores = storeRepository.multiSearch(distName, branchName,storeName,
                storeGrade, storeCd, storeChiefEmail, storeChief, brandName, storeStatus, storePType, page);
        return stores.map(this::convertToDTO);
    }




    private StoreDTO convertToDTO(Store store) {
        StoreDTO dto = modelMapper.map(store, StoreDTO.class);
        dto.setDistDTO(searchService.convertToDistDTO(store.getDist()));
        dto.setBranchDTO(searchService.convertToBranchDTO(store.getBranch()));
        dto.setBrandDTO(searchService.convertToBrandDTO(store.getBrand()));
        return dto;
    }





    public void delete(Long storeIdx){
        storeRepository.deleteById(storeIdx);
    }
}
