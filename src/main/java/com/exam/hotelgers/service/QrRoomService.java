package com.exam.hotelgers.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.exam.hotelgers.dto.DetailmenuDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.MenuCateRepository;
import com.exam.hotelgers.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QrRoomService {
    private final StoreService storeService;
    private final RoomService roomService;
    private final DetailmenuService detailmenuService;
    private  final OrderService orderService;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final SearchService searchService;
    private final MenuCateRepository menuCateRepository;

    //Application.properties에 선언한 파일이 저장될 경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    //파일저장을 위한 클래스
    private final S3Uploader s3Uploader;

//    public RoomOrderDTO Qrread(Long roomIdx) {
//        Room room = roomRepository.findById(roomIdx)
//                .orElseThrow(() -> new NotFoundException("Room not found with id: " + roomIdx));
//
//        return new RoomOrderDTO(
//                modelMapper.map(room, RoomDTO.class),
//                modelMapper.map(room.getStore(), StoreDTO.class),
//                room.getStore().getDetailMenuList().stream()
//                        .map(detailMenu -> modelMapper.map(detailMenu, DetailmenuDTO.class))
//                        .collect(Collectors.toList())
//        );
//    }


}
