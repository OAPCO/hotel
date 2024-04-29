package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.DistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Log4j2
public class BrandService {

    private final BrandRepository brandRepository;
    private final DistRepository distRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final SearchService searchService;

    public Long register(BrandDTO brandDTO) {

        //받은 distcd로 dist객체 하나 찾아서
        Optional<Dist> dist = distRepository.findByDistCd(brandDTO.getDistDTO().getDistCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }




        Brand brand = modelMapper.map(brandDTO, Brand.class);
        //brand에다가 dist를 추가해버림
        brand.setDist(dist.get());


        brandRepository.save(brand);

        return brandRepository.save(brand).getBrandIdx();
    }


    public void modify(BrandDTO brandDTO) {
        // 주어진 brandIdx를 가진 Brand를 찾습니다.
        Optional<Brand> temp = brandRepository.findByBrandIdx(brandDTO.getBrandIdx());

        if (temp.isPresent()) {
            // 입력된 brandDTO의 distCd를 기반으로 Dist를 검색합니다.
            Optional<Dist> dist = distRepository.findByDistCd(brandDTO.getDistDTO().getDistCd());

            if (!dist.isPresent()) {
                throw new IllegalArgumentException("DistCd로 검색된 Dist가 존재하지 않습니다.");
            }

            Brand brand = temp.get(); // 존재하는 Brand 찾기

            // 위에서 찾은 Dist를 Brand의 Dist로 변경합니다.
            brand.setDist(dist.get());

            // Brand 정보 업데이트
            brand.setBrandName(brandDTO.getBrandName());
            brand.setBrandCd(brandDTO.getBrandCd());

            // Brand 업데이트
            brandRepository.save(brand);
        } else {
            throw new IllegalArgumentException("입력된 brandIdx로 검색된 Brand가 존재하지 않습니다.");
        }
    }

    public BrandDTO read(Long brandIdx) {
        // brandIdx에 해당하는 Brand를 찾습니다
        Optional<Brand> brand = brandRepository.findById(brandIdx);
        BrandDTO brandDTO = null;

        if(brand.isPresent()){
            // Brand를 BrandDTO로 변환합니다
            brandDTO = modelMapper.map(brand.get(), BrandDTO.class);

            // Brand와 연관된 Dist 객체를 가져옵니다
            Dist dist = brand.get().getDist();

            // Dist를 DistDTO로 변환합니다
            // 이 때 DistChief가 있으면 DistChief도 함께 변환하도록 기능을 추가했습니다
            DistDTO distDTO = this.searchService.convertToDistDTO(dist);

            // 변환한 DistDTO를 BrandDTO에 설정해 줍니다
            brandDTO.setDistDTO(distDTO);

            // Brand와 연관된 Store 객체들을 가져옵니다
            List<Store> stores = brand.get().getStoreList();   //  변동한 부분

            // Store 목록을 StoreDTO 목록으로 변환합니다
            List<StoreDTO> storeDTOS = stores.stream().map(store -> modelMapper.map(store, StoreDTO.class)).collect(Collectors.toList());

            // 변환한 StoreDTO 목록을 BrandDTO에 설정해 줍니다
            brandDTO.setStoreDTOList(storeDTOS);
        }

        // 완성한 BrandDTO를 반환합니다
        return brandDTO;
    }



    public Page<BrandDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "brandIdx"));

        Page<Brand> brands = brandRepository.findAll(page);
        return brands.map(this::convertToDTO);
    }

    public Page<BrandDTO> searchList(String distName, String brandName,String brandCd,
                                     Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "brandIdx"));

        Page<Brand> brands = brandRepository.multisearch(distName,brandName,brandCd,page);
        return brands.map(this::convertToDTO);
    }



    private BrandDTO convertToDTO(Brand brand) {
        BrandDTO dto = modelMapper.map(brand, BrandDTO.class);
        dto.setDistDTO(convertToStoreDistDTO(brand.getDist()));
        return dto;
    }

    private DistDTO convertToStoreDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }




    public void delete(Long brandIdx){
        brandRepository.deleteById(brandIdx);
    }
}
