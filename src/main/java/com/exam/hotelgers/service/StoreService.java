package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.dto.StoreDistDTO;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.entity.StoreBranch;
import com.exam.hotelgers.entity.StoreDist;
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


    public Long register(StoreDTO storeDTO) {


        Optional<StoreDist> storeDist = storeDistRepository.findByStoreDistCode(storeDTO.getStoreDistDTO().getStoreDistName());

        
        Optional<Store> storeEntity = storeRepository
                .findByStoreCd(storeDTO.getStoreCd());

        if(storeEntity.isPresent()) {
            throw new IllegalStateException("이미 있는 코드입니다.");
        }

        Store store = modelMapper.map(storeDTO, Store.class);

        store.setStoreDist(storeDist.get());

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
        return dto;
    }

    private StoreDistDTO convertToStoreDistDTO(StoreDist storeDist) {
        return modelMapper.map(storeDist, StoreDistDTO.class);
    }



    public void delete(Long storeIdx){
        storeRepository.deleteById(storeIdx);
    }
}
