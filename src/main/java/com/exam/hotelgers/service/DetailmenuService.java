package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.DetailmenuDTO;

import com.exam.hotelgers.dto.DetailmenuDTO;
import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.MenuOption;
import com.exam.hotelgers.repository.DetailmenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class DetailmenuService {

    private final DetailmenuRepository detailmenuRepository;
    private final ModelMapper modelMapper;



    public Long register(DetailmenuDTO detailmenuDTO) {


        Detailmenu detailmenu = modelMapper.map(detailmenuDTO, Detailmenu.class);

        detailmenuRepository.save(detailmenu);

        return detailmenuRepository.save(detailmenu).getDetailmenuIdx();
    }


    public void modify(DetailmenuDTO detailmenuDTO){



        Detailmenu detailmenu = modelMapper.map(detailmenuDTO, Detailmenu.class);

        detailmenuRepository.save(detailmenu);

    }

    public DetailmenuDTO read(Long detailmenuIdx){

        Optional<Detailmenu> detailmenu= detailmenuRepository.findById(detailmenuIdx);


        return modelMapper.map(detailmenu,DetailmenuDTO.class);
    }



    public Page<DetailmenuDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"detailmenuIdx"));

        Page<Detailmenu> detailmenu = detailmenuRepository.findAll(page);


        Page<DetailmenuDTO> detailmenuDTOS = detailmenu.map(data->modelMapper.map(data,DetailmenuDTO.class));

        return detailmenuDTOS;
    }



    public void delete(Long detailmenuIdx){
        detailmenuRepository.deleteById(detailmenuIdx);
    }
}

