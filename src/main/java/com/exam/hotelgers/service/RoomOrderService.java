package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.OrderDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.RoomOrderRepository;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomOrderService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final SearchService searchService;
    private final RoomOrderRepository roomOrderRepository;
    private final RoomService roomService;

    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    private final S3Uploader s3Uploader;


    public Long register(RoomOrderDTO roomOrderDTO) {
        if(roomOrderDTO == null || roomOrderDTO.getRoomCd() == null){
            throw new IllegalArgumentException("RoomOrderDTO 또는 내부의 객실 코드는 null이 아니어야 합니다.");
        }
        String roomCd = roomOrderDTO.getRoomCd();
        Optional<Room> optionalRoom = roomRepository.findByRoomCd(roomCd);
        if(optionalRoom.isPresent()){
            Room room = optionalRoom.get();
            RoomOrder roomOrder = convertToEntity(roomOrderDTO);
            roomOrder.setRoomCd(room.getRoomCd());
            roomOrder.setStoreIdx(room.getStore().getStoreIdx());
            RoomOrder savedRoomOrder = roomOrderRepository.save(roomOrder);
            return savedRoomOrder.getRoomorderIdx();
        }
        return null;
    }

    private RoomOrder convertToEntity(RoomOrderDTO roomOrderDTO) {
        if(modelMapper.getTypeMap(RoomOrderDTO.class, RoomOrder.class) == null) {
            modelMapper.createTypeMap(RoomOrderDTO.class, RoomOrder.class)
                    .addMapping(RoomOrderDTO::getRoomCd, RoomOrder::setRoomCd)
                    .addMapping(RoomOrderDTO::getStoreIdx, RoomOrder::setStoreIdx);
        }
        RoomOrder roomOrder = modelMapper.map(roomOrderDTO, RoomOrder.class);
        return roomOrder;
    }


}
