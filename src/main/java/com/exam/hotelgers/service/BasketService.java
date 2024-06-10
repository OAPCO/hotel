package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class BasketService {

    private final ModelMapper modelMapper;
    private final BasketRepository basketRepository;




    public Long register(BasketDTO basketDTO) {

        Basket basket = modelMapper.map(basketDTO, Basket.class);

        basketRepository.save(basket);

        return basketRepository.save(basket).getBasketIdx();
    }


    public Page<BasketDTO> list(Long memberIdx,Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"basketIdx"));

        Page<Basket> baskets = basketRepository.findByMemberCart(memberIdx,pageable);


        Page<BasketDTO> BasketDTOS = baskets.map(data->modelMapper.map(data,BasketDTO.class));

        return BasketDTOS;
    }


}

