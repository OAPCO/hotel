package com.exam.hotelgers.service;


import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.StoreBranchDTO;
import com.exam.hotelgers.dto.StoreDistDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.StoreBranch;
import com.exam.hotelgers.entity.StoreBranch;
import com.exam.hotelgers.entity.StoreDist;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.StoreDistRepository;
import com.exam.hotelgers.repository.StoreBranchRepository;
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
public class StoreBranchService {

    private final StoreBranchRepository storeBranchRepository;
    private final ModelMapper modelMapper;
    private final StoreDistRepository storeDistRepository;
    private final BrandRepository brandRepository;


    public Long register(StoreBranchDTO storeBranchDTO) {


//        Optional<StoreDist> storeDist = storeDistRepository.findByStoreDistIdx(storeBranchDTO.getStoreDistDTO().getStoreDistIdx());
        Optional<StoreDist> storeDist = storeDistRepository.findByStoreDistCode(storeBranchDTO.getStoreDistDTO().getStoreDistCode());
        Optional<Brand> brand = brandRepository.findByBrandCd(storeBranchDTO.getBrandDTO().getBrandCd());

        if (!storeDist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }

        if (!brand.isPresent()) {
            throw new IllegalStateException("존재하지 않는 브랜드 코드입니다.");
        }



        Optional<StoreBranch> storeBranchOptional = storeBranchRepository
                .findByStoreBranchId(storeBranchDTO.getStoreBranchId());

        if(storeBranchOptional.isPresent()) {
            throw new IllegalStateException("이미 있는 코드입니다.");
        }

        StoreBranch storeBranch = modelMapper.map(storeBranchDTO, StoreBranch.class);

        storeBranch.setStoreDist(storeDist.get());
        storeBranch.setBrand(brand.get());


        storeBranchRepository.save(storeBranch);

        return storeBranchRepository.save(storeBranch).getStoreBranchIdx();
    }


    public void modify(StoreBranchDTO storeBranchDTO){


        Optional<StoreBranch> temp = storeBranchRepository
                .findByStoreBranchIdx(storeBranchDTO.getStoreBranchIdx());

        if(temp.isPresent()) {

            StoreBranch storeBranch = modelMapper.map(storeBranchDTO, StoreBranch.class);
            storeBranchRepository.save(storeBranch);
        }

    }

    public StoreBranchDTO read(Long storeIdx){

        Optional<StoreBranch> storeBranch= storeBranchRepository.findById(storeIdx);


        return modelMapper.map(storeBranch,StoreBranchDTO.class);
    }


    public Page<StoreBranchDTO> list(Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeBranchIdx"));

        Page<StoreBranch> stores = storeBranchRepository.findAll(page);
        return stores.map(this::convertToDTO);
    }

    private StoreBranchDTO convertToDTO(StoreBranch storeBranch) {
        StoreBranchDTO dto = modelMapper.map(storeBranch, StoreBranchDTO.class);
        dto.setStoreDistDTO(convertToStoreDistDTO(storeBranch.getStoreDist()));
        dto.setBrandDTO(convertToBrandDTO(storeBranch.getBrand()));
        return dto;
    }

    private StoreDistDTO convertToStoreDistDTO(StoreDist storeDist) {
        return modelMapper.map(storeDist, StoreDistDTO.class);
    }

    private BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }



    public void delete(Long storeIdx){
        storeBranchRepository.deleteById(storeIdx);
    }
}
