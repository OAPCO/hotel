package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuOptionDTO;

import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.MenuOption;
import com.exam.hotelgers.repository.DetailmenuRepository;
import com.exam.hotelgers.repository.MenuCateRepository;
import com.exam.hotelgers.repository.MenuoptionRepository;
import com.exam.hotelgers.repository.StoreRepository;
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
    private final DetailmenuService detailmenuService;
    private final DetailmenuRepository detailmenuRepository;



    public Long register(MenuOptionDTO menuOptionDTO) {

        // 상세 메뉴를 menuDetailRepository에서 찾습니다.
        Optional<Detailmenu> detailmenu=
                detailmenuRepository.findByDetailmenuIdx(menuOptionDTO.getDetailmenuDTO().getDetailmenuIdx());

        // 찾는 상세 메뉴가 없으면 예외를 던집니다
//        if (!detailmenu.isPresent()) {
//            throw new IllegalStateException("존재하지 않는 상세 메뉴 번호입니다.");
//        }

        // MenuOptionDTO를 MenuOption으로 매핑하고,
        MenuOption menuOption = modelMapper.map(menuOptionDTO, MenuOption.class);

        // 찾은 Menudetail를 MenuOption에 설정합니다.
        menuOption.setDetailmenu(detailmenu.get());

        // 변경된 menuOption을 저장하고
        menuoptionRepository.save(menuOption);

        // 저장한 후의 MenuOption Idx를 반환합니다.
        return menuoptionRepository.save(menuOption).getMenuOptionIdx();
    }


    public void modify(MenuOptionDTO menuOptionDTO){

        // MenuOptionIdx를 사용하여 존재하는 MenuOption을 찾습니다.
        Optional<MenuOption> temp = menuoptionRepository.findByMenuOptionIdx(menuOptionDTO.getMenuOptionIdx());

        // MenuOption이 존재하는 경우에만 수정 작업을 진행합니다.
        if(temp.isPresent()) {
            // DTO에서 존재하는 MenuOption 객체로 변환합니다.
            MenuOption menuOption = modelMapper.map(menuOptionDTO, MenuOption.class);
            // 변경사항을 데이터베이스에 저장합니다.
            menuoptionRepository.save(menuOption);
        }
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

