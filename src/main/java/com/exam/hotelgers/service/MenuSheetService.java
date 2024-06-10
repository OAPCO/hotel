package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class MenuSheetService {

    private final MenuSheetRepository menuSheetRepository;
    private final ModelMapper modelMapper;




    //메뉴
    public List<MenuSheetDTO> menuSearch(Long menuOrderIdx) {


        List<MenuSheet> menuSheetList = menuSheetRepository.findByMenuOrderMenu(menuOrderIdx);


        List<MenuSheetDTO> MenuSheetDTOs = menuSheetList.stream()
                .map(menuSheet -> modelMapper.map(menuSheet, MenuSheetDTO.class))
                .collect(Collectors.toList());

        return MenuSheetDTOs;
    }


}
