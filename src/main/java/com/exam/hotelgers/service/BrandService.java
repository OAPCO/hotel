package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.distDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.BranchRepository;
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

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Log4j2
public class BrandService {

    private final BrandRepository brandRepository;
    private final DistRepository distRepository;
    private final BranchRepository branchRepository;
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

    public BrandDTO read(Long brandIdx){

        Optional<Brand> brand= brandRepository.findById(brandIdx);


        return modelMapper.map(brand,BrandDTO.class);
    }



    public Page<BrandDTO> list(Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "brandIdx"));

        Page<Brand> brands = brandRepository.findAll(page);
        return brands.map(this::convertToDTO);
    }

    private BrandDTO convertToDTO(Brand brand) {
        BrandDTO dto = modelMapper.map(brand, BrandDTO.class);
        dto.setDistDTO(convertToStoreDistDTO(brand.getDist()));
        return dto;
    }

    private distDTO convertToStoreDistDTO(Dist dist) {
        return modelMapper.map(dist, distDTO.class);
    }



    public void delete(Long brandIdx){
        brandRepository.deleteById(brandIdx);
    }
}
