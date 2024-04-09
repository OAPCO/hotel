package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.SelectMenuDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.SelectMenu;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.SelectMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SelectMenuService {
    private final SelectMenuRepository selectMenuRepository;
    private final ModelMapper modelMapper;

    public Long register(SelectMenuDTO selectMenuDTO) {


        SelectMenu selectMenu = modelMapper.map(selectMenuDTO, SelectMenu.class);

        selectMenuRepository.save(selectMenu);

        return selectMenuRepository.save(selectMenu).getSelectMenuIdx();
    }


    public void modify(SelectMenuDTO selectMenuDTO){




        Optional<SelectMenu> temp = selectMenuRepository
                .findBySelectMenuIdx(selectMenuDTO.getMenuSheetNo());


        if(temp.isPresent()) {

            SelectMenu selectMenu = modelMapper.map(selectMenuDTO, SelectMenu.class);

            selectMenuRepository.save(selectMenu);
        }


    }

    public SelectMenuDTO read(Long selectMenuIdx){

        Optional<SelectMenu> selectMenu= selectMenuRepository.findById(selectMenuIdx);


        return modelMapper.map(selectMenu,SelectMenuDTO.class);
    }



    public Page<SelectMenuDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"selectMenuIdx"));

        Page<SelectMenu> selectMenus = selectMenuRepository.findAll(page);


        Page<SelectMenuDTO> selectMenuDTOS = selectMenus.map(data->modelMapper.map(data,SelectMenuDTO.class));


        return selectMenuDTOS;
    }



    public void delete(Long selectMenuIdx){
        selectMenuRepository.deleteById(selectMenuIdx);
    }
}
