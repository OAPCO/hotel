package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.DetailmenuDTO;
import com.exam.hotelgers.dto.MenuCateDTO;

import com.exam.hotelgers.dto.MenuCateDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.MenuCate;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.repository.MenuCateRepository;
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


    public List<MenuCateDTO> loginManagerMenuCateSearch(Long storeIdx) {
        List<MenuCate> menuCates = menuCateRepository.loginManagerMenuCateSearch(storeIdx);

        List<MenuCateDTO> menuCateDTOS = menuCates.stream()
                .map(menuCate -> {
                    // Fetch or load the detail menu list from the menuCate object
                    List<Detailmenu> detailmenus = menuCate.getDetailMenuList();

                    MenuCateDTO menuCateDTO = modelMapper.map(menuCate, MenuCateDTO.class);

                    // Map the detail menus to detail menu DTOs and set the list in menuCateDTO
                    List<DetailmenuDTO> detailMenuDTOs = detailmenus.stream()
                            .map(detailmenu -> modelMapper.map(detailmenu, DetailmenuDTO.class))
                            .collect(Collectors.toList());

                    menuCateDTO.setDetailMenuDTOList(detailMenuDTOs);
                    return menuCateDTO;
                })
                .collect(Collectors.toList());

        return menuCateDTOS;
    }
}

