package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuOptionDTO;

import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.entity.MenuOption;
import com.exam.hotelgers.repository.MenuCateRepository;
import com.exam.hotelgers.repository.MenuoptionRepository;
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
public class MenuOptionService {

    private final MenuoptionRepository menuoptionRepository;
    private final ModelMapper modelMapper;



    public Long register(MenuOptionDTO menuOptionDTO) {


        MenuOption menuOption = modelMapper.map(menuOptionDTO, MenuOption.class);

        menuoptionRepository.save(menuOption);

        return menuoptionRepository.save(menuOption).getMenuOptionIdx();
    }


    public void modify(MenuOptionDTO menuOptionDTO){



        MenuOption menuOption = modelMapper.map(menuOptionDTO, MenuOption.class);

        menuoptionRepository.save(menuOption);

    }

    public MenuOptionDTO read(Long menuOptionIdx){

        Optional<MenuOption> menuOption= menuoptionRepository.findById(menuOptionIdx);


        return modelMapper.map(menuOption,MenuOptionDTO.class);
    }



    public Page<MenuOptionDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"menuOptionIdx"));

        Page<MenuOption> menuOptions = menuoptionRepository.findAll(page);


        Page<MenuOptionDTO> menuoptionDTOS = menuOptions.map(data->modelMapper.map(data,MenuOptionDTO.class));

        return menuoptionDTOS;
    }



    public void delete(Long menuOptionIdx){
        menuoptionRepository.deleteById(menuOptionIdx);
    }
}

