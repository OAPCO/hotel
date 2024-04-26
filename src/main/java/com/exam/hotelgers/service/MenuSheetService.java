package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.MenuSheetDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.MenuSheet;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.repository.MenuSheetRepository;
import com.exam.hotelgers.repository.RoomRepository;
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
public class MenuSheetService {

    private final MenuSheetRepository menuSheetRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;






    //등록
    public Long register(MenuSheetDTO menuSheetDTO) {

        
        Optional<Store> store = storeRepository.findByStoreCd(menuSheetDTO.getStoreDTO().getStoreCd());
        Optional<Room> room = roomRepository.findByRoomCd(menuSheetDTO.getRoomDTO().getRoomCd());

        
        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }
        if (!room.isPresent()) {
            throw new IllegalStateException("존재하지 않는 룸 코드입니다.");
        }




        Optional<MenuSheet> temp = menuSheetRepository.findByNewOrderNo(menuSheetDTO.getNewOrderNo());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }


        MenuSheet menuSheet = modelMapper.map(menuSheetDTO, MenuSheet.class);

        
        menuSheet.setStore(store.get());
        menuSheet.setRoom(room.get());


        modelMapper.map(store.get(), menuSheetDTO.getStoreDTO());
        modelMapper.map(room.get(), menuSheetDTO.getRoomDTO());



        return menuSheetRepository.save(menuSheet).getMenuSheetIdx();
    }

    

    //수정
    public void modify(MenuSheetDTO menuSheetDTO){


        Optional<MenuSheet> temp = menuSheetRepository
                .findByMenuSheetIdx(menuSheetDTO.getMenuSheetIdx());

        if(temp.isPresent()) {

            MenuSheet order = modelMapper.map(menuSheetDTO, MenuSheet.class);
            menuSheetRepository.save(order);
        }

    }



    //삭제
    public void delete(Long orderIdx){
        menuSheetRepository.deleteById(orderIdx);
    }



    public MenuSheetDTO read(Long id) {
        Optional<MenuSheet> storeMember = menuSheetRepository.findByMenuSheetIdx(id);
        //SalesDTO result = modelMapper.map(storeMember, SalesDTO.class);
        MenuSheetDTO result = storeMember.map(data->modelMapper.map(data, MenuSheetDTO.class)).orElse(null);

        return result;
    }





    public Page<MenuSheetDTO> searchList(Pageable pageable, MenuSheetDTO menuSheetDTO) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt,
                Sort.by(Sort.Direction.DESC,"menuSheetIdx"));

        Page<MenuSheet> menuSheets = menuSheetRepository.menuSheetListSearch(
                menuSheetDTO.getStoreDTO().getStoreName(),  //매장명
                menuSheetDTO.getRoomDTO().getRoomCd(), //룸 코드
                menuSheetDTO.getNewOrderNo(), //신규 주문번호
                menuSheetDTO.getMenuSheetState(),//주문서 상태 0.주문전, 1.조리요청, 2.결제요청, 3.결제완료, 4.결제취소, 5.조리완료, 6.배달요청, 7.배달완료
                menuSheetDTO.getStartDate(),//시작날짜
                menuSheetDTO.getEndDate(),//종료날짜
                menuSheetDTO.getOrderProgressStatus(),//주문상태(NEW 신규,CHECK 접수,CANCEL 취소,CALL 호출,CLOSE 완료)
                menuSheetDTO.getMenuSheetName(),//주문서 이름
                pageable);


        Page<MenuSheetDTO> result = menuSheets.map(data->modelMapper.map(data,MenuSheetDTO.class));

        return result;
    }
}
