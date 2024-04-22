package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;



    public Long register(RoomDTO roomDTO) {

        Optional<Store> store = storeRepository.findByStoreIdx(roomDTO.getStoreDTO().getStoreIdx());


        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 번호입니다.");
        }



        Optional<Room> temp = roomRepository
                .findByRoomCd(roomDTO.getRoomCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }



        Room room = modelMapper.map(roomDTO, Room.class);

        room.setStore(store.get());



        roomRepository.save(room);

        return roomRepository.save(room).getRoomIdx();
    }



    public void modify(RoomDTO roomDTO){


        Optional<Room> temp = roomRepository
                .findByRoomIdx(roomDTO.getRoomIdx());

        if(temp.isPresent()) {

            Room room = modelMapper.map(roomDTO, Room.class);
            roomRepository.save(room);
        }

    }




    public RoomDTO read(Long roomIdx) {
        Optional<Room> roomEntityOptional = roomRepository.findById(roomIdx);
        if (roomEntityOptional.isPresent()) {
            Room room = roomEntityOptional.get();
            RoomDTO dto = modelMapper.map(room, RoomDTO.class);
            dto.setStoreDTO(convertToRoomStoreDTO(room.getStore()));
            dto.setOrderDTOList(convertOrderToDTOs(room.getOrderList()));
            return dto;

        } else {
            return null;
        }
    }




    private List<OrderDTO> convertOrderToDTOs(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }






    public Page<RoomDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "roomIdx"));

        Page<Room> rooms = roomRepository.findAll(page);
        return rooms.map(this::convertToDTO);
    }





    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = modelMapper.map(room, RoomDTO.class);
        dto.setStoreDTO(convertToRoomStoreDTO(room.getStore()));
        return dto;
    }



    private StoreDTO convertToRoomStoreDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }





    public void delete(Long roomIdx){
        roomRepository.deleteById(roomIdx);
    }
}
