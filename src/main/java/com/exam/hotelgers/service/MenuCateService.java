package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuCateDTO;

import com.exam.hotelgers.dto.MenuCateDTO;
import com.exam.hotelgers.entity.MenuCate;
import com.exam.hotelgers.repository.MenuCateRepository;
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
public class MenuCateService {

    private final MenuCateRepository menuCateRepository;
    private final ModelMapper modelMapper;



    public Long register(MenuCateDTO menuCateDTO) {


        MenuCate menuCate = modelMapper.map(menuCateDTO, MenuCate.class);

        menuCateRepository.save(menuCate);

        return menuCateRepository.save(menuCate).getMenuCateIdx();
    }


    public void modify(MenuCateDTO menuCateDTO){



        MenuCate menuCate = modelMapper.map(menuCateDTO, MenuCate.class);

        menuCateRepository.save(menuCate);

    }

    public MenuCateDTO read(Long menuCateIdx){

        Optional<MenuCate> menuCate= menuCateRepository.findById(menuCateIdx);


        return modelMapper.map(menuCate,MenuCateDTO.class);
    }



    public Page<MenuCateDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"menuCateIdx"));

        Page<MenuCate> menuCates = menuCateRepository.findAll(page);


        Page<MenuCateDTO> menucateDTOS = menuCates.map(data->modelMapper.map(data,MenuCateDTO.class));

        return menucateDTOS;
    }



    public void delete(Long menuCateIdx){
        menuCateRepository.deleteById(menuCateIdx);
    }
}

