package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.RoomOrderRepository;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RoomOrderService {

    private final ModelMapper modelMapper;
    private final RoomOrderRepository roomOrderRepository;

    @Value("${imgUploadLocation}")
    private String imgUploadLocation;
    private final S3Uploader s3Uploader;


    public Long register(RoomOrderDTO roomOrderDTO){

        Optional<RoomOrder> roomOrderCheck = roomOrderRepository.roomOrderIspresentCheck(roomOrderDTO.getRoomorderIdx());

        if(roomOrderCheck.isPresent()) {
            throw new IllegalStateException("이미 예약된 방입니다.");
        }

        //1=예약상태
        roomOrderDTO.setRoomStatus(1);


        RoomOrder roomOrder = modelMapper.map(roomOrderDTO, RoomOrder.class);

        return roomOrderRepository.save(roomOrder).getRoomorderIdx();

    }





    public List<RoomOrderDTO> roomOrderSearch2(Long storeIdx){


        List<RoomOrder> roomOrders = roomOrderRepository.roomOrderSearch2(storeIdx);


        List<RoomOrderDTO> roomOrderDTOs = roomOrders.stream()
                .map(roomOrder -> modelMapper.map(roomOrder, RoomOrderDTO.class))
                .collect(Collectors.toList());

        return roomOrderDTOs;

    }





    public List<RoomOrderDTO> RoomOrderAllSearch(SearchDTO searchDTO){


        List<RoomOrder> roomOrders = roomOrderRepository.RoomOrderAllSearch(searchDTO);


        List<RoomOrderDTO> roomOrderDTOs = roomOrders.stream()
                .map(roomOrder -> modelMapper.map(roomOrder, RoomOrderDTO.class))
                .collect(Collectors.toList());

        return roomOrderDTOs;

    }






    public RoomOrderDTO roomOrder2Check(Long roomIdx){

        Optional<RoomOrder> roomOrder = roomOrderRepository.roomOrder2Check(roomIdx);

        return modelMapper.map(roomOrder,RoomOrderDTO.class);
    }



    //회원이 현재 묵고 있는 방 찾는 쿼리
    public RoomOrderDTO findmemberInRoomOrder(Long memberIdx){

        Optional<RoomOrder> roomOrder = roomOrderRepository.findmemberInRoomOrder(memberIdx);

        return modelMapper.map(roomOrder,RoomOrderDTO.class);
    }




    @Transactional
    public void roomOrderStatusUpdate2(Long roomIdx, Long roomorderIdx, LocalDateTime checkinTime) {
        roomOrderRepository.roomOrderStatusUpdate2(roomIdx, roomorderIdx,checkinTime);
    }

    @Transactional
    public void roomCheckOut(Long roomIdx) {
        roomOrderRepository.roomCheckOut(roomIdx);
    }





    public List<RoomOrderDTO> roomOrderCheck(SearchDTO searchDTO){


        List<RoomOrder> roomOrders = roomOrderRepository.searchRoomOrder(searchDTO);


        List<RoomOrderDTO> roomOrderDTOs = roomOrders.stream()
                .map(roomOrder -> modelMapper.map(roomOrder, RoomOrderDTO.class))
                .collect(Collectors.toList());

        return roomOrderDTOs;

    }





    //객실예약취소
    @Transactional
    public void roomOrderDelete(Long roomorderIdx) {
        roomOrderRepository.roomOrderDelete(roomorderIdx);
    }


}
