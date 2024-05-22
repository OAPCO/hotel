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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
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



    public Page<RoomOrderDTO> roomOrderSearch(Pageable pageable,Long storeIdx){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"roomorderIdx"));

        Page<RoomOrder> roomOrders = roomOrderRepository.roomOrderSearch(pageable,storeIdx);

        return roomOrders.map(data->modelMapper.map(data,RoomOrderDTO.class));
    }


    public List<RoomOrderDTO> roomOrderSearch2(Long storeIdx){


        List<RoomOrder> roomOrders = roomOrderRepository.roomOrderSearch2(storeIdx);


        List<RoomOrderDTO> roomOrderDTOs = roomOrders.stream()
                .map(roomOrder -> modelMapper.map(roomOrder, RoomOrderDTO.class))
                .collect(Collectors.toList());

        return roomOrderDTOs;

    }


    public Page<RoomOrderDTO> endRoomOrderSearch(Pageable pageable,SearchDTO searchDTO){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"roomorderIdx"));

        Page<RoomOrder> roomOrders = roomOrderRepository.endRoomOrderSearch(pageable,searchDTO);

        return roomOrders.map(data->modelMapper.map(data,RoomOrderDTO.class));
    }


    public RoomOrderDTO roomOrderStatusCheck(SearchDTO searchDTO){

        Optional<RoomOrder> roomOrder = roomOrderRepository.roomOrderStatusCheck(searchDTO);

        return modelMapper.map(roomOrder,RoomOrderDTO.class);
    }


    public MemberDTO roomOrderMemberCheck(SearchDTO searchDTO){

        Optional<Member> member = roomOrderRepository.roomOrderMemberCheck(searchDTO);

        return modelMapper.map(member,MemberDTO.class);
    }


    @Transactional
    public void roomOrderStatusUpdate2(Long roomIdx, Long roomorderIdx) {
        roomOrderRepository.roomOrderStatusUpdate2(roomIdx, roomorderIdx);
    }




    public List<RoomOrderDTO> roomOrderCheck(SearchDTO searchDTO){


        List<RoomOrder> roomOrders = roomOrderRepository.searchRoomOrder(searchDTO);


        List<RoomOrderDTO> roomOrderDTOs = roomOrders.stream()
                .map(roomOrder -> modelMapper.map(roomOrder, RoomOrderDTO.class))
                .collect(Collectors.toList());

        return roomOrderDTOs;

    }



}
