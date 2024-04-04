package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.entity.Banner;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.repository.BrandRepository;
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
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public Long register(BrandDTO brandDTO) {
        

        Brand brand = modelMapper.map(brandDTO, Brand.class);

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
    


    public Page<BrandDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"brandIdx"));

        Page<Brand> brands = brandRepository.findAll(page);


        Page<BrandDTO> brandDTOS = brands.map(data->modelMapper.map(data,BrandDTO.class));


        return brandDTOS;
    }



    public void delete(Long brandIdx){
        brandRepository.deleteById(brandIdx);
    }
}
