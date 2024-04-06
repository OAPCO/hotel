package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.StoreBranchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.dto.StoreDistDTO;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.entity.StoreBranch;
import com.exam.hotelgers.entity.StoreDist;
import com.exam.hotelgers.repository.StoreBranchRepository;
import com.exam.hotelgers.repository.StoreDistRepository;
import com.exam.hotelgers.repository.StoreRepository;
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
public class StoreService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final StoreDistRepository storeDistRepository;
    private final StoreBranchRepository storeBranchRepository;



    public Long register(StoreDTO storeDTO) {


        Optional<StoreDist> storeDist = storeDistRepository.findByStoreDistIdx(storeDTO.getStoreDistDTO().getStoreDistIdx());
        Optional<StoreBranch> storeBranch = storeBranchRepository.findByStoreBranchIdx(storeDTO.getStoreBranchDTO().getStoreBranchIdx());


        if (!storeDist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 번호입니다.");
        }
        if (!storeBranch.isPresent()) {
            throw new IllegalStateException("존재하지 않는 지사 번호입니다.");
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


        store.setStoreDist(storeDist.get());
        store.setStoreBranch(storeBranch.get());

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

    public StoreDTO read(Long storeIdx){

        Optional<Store> store= storeRepository.findById(storeIdx);


        return modelMapper.map(store,StoreDTO.class);
    }


    public Page<StoreDTO> list(Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));

        Page<Store> stores = storeRepository.findAll(page);
        return stores.map(this::convertToDTO);
    }

    private StoreDTO convertToDTO(Store store) {
        StoreDTO dto = modelMapper.map(store, StoreDTO.class);
        dto.setStoreDistDTO(convertToStoreDistDTO(store.getStoreDist()));
        dto.setStoreBranchDTO(convertToStoreBranchDTO(store.getStoreBranch()));
        return dto;
    }

    private StoreDistDTO convertToStoreDistDTO(StoreDist storeDist) {
        return modelMapper.map(storeDist, StoreDistDTO.class);
    }

    private StoreBranchDTO convertToStoreBranchDTO(StoreBranch storeBranch) {
        return modelMapper.map(storeBranch, StoreBranchDTO.class);
    }



    public void delete(Long storeIdx){
        storeRepository.deleteById(storeIdx);
    }
}
