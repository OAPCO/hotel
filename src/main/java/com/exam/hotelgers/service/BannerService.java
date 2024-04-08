package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.entity.Banner;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.BannerRepository;
import com.exam.hotelgers.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
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
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public Long register(BannerDTO bannerDTO) {
        

        Banner banner = modelMapper.map(bannerDTO, Banner.class);

        bannerRepository.save(banner);

        return bannerRepository.save(banner).getBannerIdx();
    }


    public void modify(BannerDTO bannerDTO){


        Optional<Banner> temp = bannerRepository
                .findByBannerIdx(bannerDTO.getBannerIdx());


        if(temp.isPresent()) {

            Banner banner = modelMapper.map(bannerDTO, Banner.class);

            bannerRepository.save(banner);
        }


    }

    public BannerDTO read(Long bannerIdx){

        Optional<Banner> banner= bannerRepository.findById(bannerIdx);


        return modelMapper.map(banner,BannerDTO.class);
    }
    


    public Page<BannerDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"bannerIdx"));

        Page<Banner> banners = bannerRepository.findAll(page);


        Page<BannerDTO> bannerDTOS = banners.map(data->modelMapper.map(data,BannerDTO.class));

        return bannerDTOS;
    }



    public void delete(Long bannerIdx){
        bannerRepository.deleteById(bannerIdx);
    }
}
