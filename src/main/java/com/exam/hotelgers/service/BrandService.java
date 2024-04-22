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


    public Long register(BrandDTO brandDTO) {


        Optional<Dist> dist = distRepository.findByDistCd(brandDTO.getDistDTO().getDistCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }




        Brand brand = modelMapper.map(brandDTO, Brand.class);

        brand.setDist(dist.get());


        brandRepository.save(brand);

        return brandRepository.save(brand).getBrandIdx();
    }


    public void modify(BrandDTO brandDTO){




        Optional<Brand> temp = brandRepository
                .findByBrandIdx(brandDTO.getBrandIdx());


        if(temp.isPresent()) {

            Brand brand = modelMapper.map(brandDTO, Brand.class);

            brandRepository.save(brand);
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
            DistDTO distDTO = modelMapper.map(dist, DistDTO.class);

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

    public Page<BrandDTO> searchList(String distChiefEmail, String distName, String brandName,String brandCd,
                                     StoreStatus storestatus,
                                     Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "brandIdx"));

        Page<Brand> brands = brandRepository.multisearch(distChiefEmail, distName,brandName,brandCd,storestatus, page);
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
