package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.OrderDTO;
import com.exam.hotelgers.dto.QnaDTO;
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


    public Long register(RoomOrderDTO roomOrderDTO){


        RoomOrder roomOrder = modelMapper.map(roomOrderDTO, RoomOrder.class);

        return roomOrderRepository.save(roomOrder).getRoomorderIdx();

    }



}
