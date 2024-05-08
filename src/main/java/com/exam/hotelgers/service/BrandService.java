package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final DistChiefRepository distChiefRepository;
    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;



    public Long register(BrandDTO brandDTO) {


        Optional<Dist> dist = distRepository.findByDistCd(brandDTO.getDistCd());

        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }



        Brand brand = modelMapper.map(brandDTO, Brand.class);

        brandRepository.save(brand);

        return brandRepository.save(brand).getBrandIdx();
    }


    public void modify(BrandDTO brandDTO) {
        // 주어진 brandIdx를 가진 Brand를 찾습니다.
        Optional<Brand> temp = brandRepository.findByBrandIdx(brandDTO.getBrandIdx());

        if (temp.isPresent()) {
            // 입력된 brandDTO의 distCd를 기반으로 Dist를 검색합니다.
            Optional<Dist> dist = distRepository.findByDistCd(brandDTO.getDistCd());

            if (!dist.isPresent()) {
                throw new IllegalArgumentException("DistCd로 검색된 Dist가 존재하지 않습니다.");
            }

            Brand brand = temp.get(); // 존재하는 Brand 찾기

            // 위에서 찾은 Dist를 Brand의 Dist로 변경합니다.
//            brand.setDist(dist.get());

            // Brand 정보 업데이트
            brand.setBrandName(brandDTO.getBrandName());
            brand.setBrandCd(brandDTO.getBrandCd());

            // Brand 업데이트
            brandRepository.save(brand);
        } else {
            throw new IllegalArgumentException("입력된 brandIdx로 검색된 Brand가 존재하지 않습니다.");
        }
    }

    public Map<String, Object> read(Long brandIdx) {
        Map<String, Object> modelMap = new HashMap<>();
        List<Object[]> results = brandRepository.brandManagerDistDistChief(brandIdx);

        if (results.size() > 0) {
            Object[] result = results.get(0); // assuming only one result
            Brand brand = (Brand) result[0];
            Manager manager = (Manager) result[1];
            Dist dist = (Dist) result[2];
            DistChief distChief = (DistChief) result[3];
            modelMap.put("brand", brand);
            modelMap.put("manager", manager);
            modelMap.put("dist", dist);
            modelMap.put("distChief", distChief);

            // Store 정보를 brandCd를 기반으로 가져옴
            String brandCd = brand.getBrandCd();
            List<Store> storeList = storeRepository.findByBrandCd(brandCd);
            modelMap.put("storeList", storeList);
        }  else {
            log.info("아이디" + brandIdx + "에 해당하는 정보를 찾지 못했습니다.");
        }
        return modelMap;
    }



    public Page<BrandDTO> list(Pageable pageable, Principal principal) {

        String userId = principal.getName();

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "brandIdx"));

        Page<Object[]> brands = brandRepository.distChiefToBrandSearch(page,userId);

        return brands.map(this::convertToDTO);
    }


    private BrandDTO convertToDTO(Object[] result) {
        Brand brand = (Brand) result[0];
        Dist dist = (Dist) result[1];
        BrandDTO dto = modelMapper.map(brand, BrandDTO.class);
        dto.setDistDTO(modelMapper.map(dist, DistDTO.class));

        return dto;
    }




    public void delete(Long brandIdx){
        brandRepository.deleteById(brandIdx);
    }



    public List<BrandDTO> distOfBrand(SearchDTO searchDTO) {

        List<Brand> brands = brandRepository.distOfBrand(searchDTO);
        return brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }

}