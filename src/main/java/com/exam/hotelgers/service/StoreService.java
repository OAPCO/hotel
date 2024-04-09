package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.branchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.dto.distDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Dist;
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

import java.util.Optional;

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



    public Long register(StoreDTO storeDTO) {


//        Optional<Dist> dist = distRepository.findByStoreDistIdx(storeDTO.getStoreDistDTO().getStoreDistIdx());
//        Optional<Branch> branch = branchRepository.findByStoreBranchIdx(storeDTO.getStoreBranchDTO().getStoreBranchIdx());
//        Optional<Brand> brand = brandRepository.findByBrandIdx(storeDTO.getBrandDTO().getBrandIdx());

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
        dto.setDistDTO(convertToStoreDistDTO(store.getDist()));
        dto.setBranchDTO(convertToStoreBranchDTO(store.getBranch()));
        dto.setBrandDTO(convertToBrandDTO(store.getBrand()));
        return dto;
    }

    private distDTO convertToStoreDistDTO(Dist dist) {

        return modelMapper.map(dist, distDTO.class);
    }

    private branchDTO convertToStoreBranchDTO(Branch branch) {
        return modelMapper.map(branch, branchDTO.class);
    }

    private BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }




    public void delete(Long storeIdx){
        storeRepository.deleteById(storeIdx);
    }
}
