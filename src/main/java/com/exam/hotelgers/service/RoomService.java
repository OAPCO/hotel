package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    //Application.properties에 선언한 파일이 저장될 경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    //파일저장을 위한 클래스
    private final S3Uploader s3Uploader;


    public Long register(RoomDTO roomDTO, MultipartFile imgFile) throws IOException {

        Optional<Store> store = storeRepository.findByStoreCd(roomDTO.getStoreDTO().getStoreCd());


        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }



        Optional<Room> temp = roomRepository
                .findByRoomCd(roomDTO.getRoomCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }

        String originalFileName = imgFile.getOriginalFilename(); //저장할 파일명
        String newFileName = ""; //새로 만든 파일명

        if(originalFileName != null) { //파일이 존재하면
            newFileName = s3Uploader.upload(imgFile,imgUploadLocation);
        }

        roomDTO.setRoomimgName(newFileName); //새로운 파일명을 재등록

        Room room = modelMapper.map(roomDTO, Room.class);

        room.setStore(store.get());



        roomRepository.save(room);

        return roomRepository.save(room).getRoomIdx();
    }



    public void modify(RoomDTO roomDTO, @Nullable MultipartFile imgFile) throws IOException {

        Optional<Room> temp = roomRepository.findByRoomIdx(roomDTO.getRoomIdx());

        if(temp.isPresent()) {
            Room room = temp.get();

            // 이미지 파일이 제공된 경우에만 기존 이미지 삭제 및 새 이미지 업로드 진행
            if (imgFile != null && !imgFile.isEmpty()) {

                // 기존에 업로드된 이미지가 있다면 삭제
                if (room.getRoomimgName() != null && !room.getRoomimgName().isEmpty()) {
                    s3Uploader.deleteFile(room.getRoomimgName(), imgUploadLocation);
                }

                // 새 이미지를 업로드하고, 새 파일명을 RoomDTO에 저장
                String newFileName = s3Uploader.upload(imgFile,imgUploadLocation);
                room.setRoomimgName(newFileName);
            }

            // RoomDTO를 Room 엔티티로 매핑하고 저장
            room = modelMapper.map(roomDTO, Room.class);
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





    public void delete(Long roomIdx) throws IOException {

        //물리적위치에 저장된 이미지를 삭제
        Room room = roomRepository
                .findById(roomIdx)
                .orElseThrow();; //조회->저장
        //deleteFile(파일명, 폴더명)
        s3Uploader.deleteFile(room.getRoomimgName(), imgUploadLocation);

        roomRepository.deleteById(roomIdx);
    }
}
