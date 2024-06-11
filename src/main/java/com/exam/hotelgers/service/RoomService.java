package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.ImageRepository;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final SearchService searchService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    private final S3Uploader s3Uploader;


    public Long register(RoomDTO roomDTO, List<MultipartFile> imgFiles, MultipartFile mainimgFile) throws IOException {

        Optional<Store> store = storeRepository.storeNameSearch(roomDTO.getStoreDTO().getStoreName());


        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }


        Optional<Room> roomCdCheck = roomRepository.findByRoomCd(roomDTO.getRoomCd());
        Optional<Room> roomTypeCheck = roomRepository.findByRoomCd(roomDTO.getRoomCd());


        Room room = modelMapper.map(roomDTO, Room.class);

        room.setRoomStatus(0);
        room.setStore(store.get());


        Long roomIdx = roomRepository.save(room).getRoomIdx();

        String roomType = room.getRoomType();

        imageService.roomImageregister(imgFiles, store.get().getStoreIdx(),roomType);

        //대표파일의 이름을 반환받아온다.
        String imgName = imageService.roomMainImageregister(mainimgFile, store.get().getStoreIdx(), roomType);

        //그 이름을 엔티티 대표이미지속성에 집어넣는다.
        room.setRoomMainimgName(imgName);

        //다시 저장한다.
        roomRepository.save(room);

        return roomIdx;
    }


    public Long registeradd(RoomDTO roomDTO) throws IOException {

        Optional<Store> store = storeRepository.storeNameSearch(roomDTO.getStoreDTO().getStoreName());


        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }





        Room room = modelMapper.map(roomDTO, Room.class);

        room.setRoomStatus(0);
        room.setStore(store.get());


        Long roomIdx = roomRepository.save(room).getRoomIdx();

        return roomIdx;
    }



    public void modify(RoomDTO newRoom, @Nullable MultipartFile imgFile) throws IOException {

        Optional<Room> optionalRoom = roomRepository.findByRoomIdx(newRoom.getRoomIdx());

        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();

            if (imgFile != null && !imgFile.isEmpty()) {
                // 만약 방에 이미 이미지가 있다면 S3에서 그 이미지를 삭제합니다.
                if (room.getRoomimgName() != null && !room.getRoomimgName().isEmpty()) {
                    s3Uploader.deleteFile(room.getRoomimgName(), imgUploadLocation);
                }

                // S3에 새 파일을 업로드하고 방의 이미지 이름 속성을 업데이트 합니다.
                String newFileName = s3Uploader.upload(imgFile, imgUploadLocation);
                room.setRoomimgName(newFileName);
            }

            // DTO로부터 필드들을 직접 업데이트 합니다.
            room.setRoomCd(newRoom.getRoomCd());
            room.setRoomName(newRoom.getRoomName());
            room.setRoomType(newRoom.getRoomType());

            // 업데이트 된 방을 저장합니다.
            roomRepository.save(room);
        } else {
            throw new IllegalArgumentException("해당 방이 존재하지 않습니다.");
        }
    }


    //읽기
    public RoomDTO read(Long roomIdx) {
        Optional<Room> roomEntityOptional = roomRepository.findById(roomIdx);
        if (roomEntityOptional.isPresent()) {
            Room room = roomEntityOptional.get();
            RoomDTO dto = modelMapper.map(room, RoomDTO.class);
            dto.setStoreDTO(searchService.convertToStoreDTO(room.getStore()));
            dto.setMenuOrderDTOList(convertOrderToDTOs(room.getMenuOrderList()));
            return dto;

        } else {
            return null;
        }
    }


    private List<MenuOrderDTO> convertOrderToDTOs(List<MenuOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, MenuOrderDTO.class))
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
        dto.setStoreDTO(searchService.convertToStoreDTO(room.getStore()));
        return dto;
    }


    public void delete(Long roomIdx) throws IOException {

        Room room = roomRepository
                .findById(roomIdx)
                .orElseThrow();

        s3Uploader.deleteFile(room.getRoomimgName(), imgUploadLocation);

        roomRepository.deleteById(roomIdx);
    }


    public RoomDTO userRoom(Long roomIdx) {
        Optional<Room> roomEntityOptional = roomRepository.findByRoomIdx(roomIdx);

        if (roomEntityOptional.isPresent()) {
            Room room = roomEntityOptional.get();
            RoomDTO dto = modelMapper.map(room, RoomDTO.class);

            StoreDTO storeDTO = modelMapper.map(room.getStore(), StoreDTO.class);

            List<DetailmenuDTO> detailMenuDTOList = room.getStore().getDetailMenuList()
                    .stream()
                    .map(detailMenu -> modelMapper.map(detailMenu, DetailmenuDTO.class))
                    .collect(Collectors.toList());

            storeDTO.setDetailmenuDTOList(detailMenuDTOList);
            dto.setStoreDTO(storeDTO);
            dto.setMenuOrderDTOList(room.getMenuOrderList().stream()
                    .map(order -> modelMapper.map(order, MenuOrderDTO.class))
                    .collect(Collectors.toList()));

            return dto;

        } else {
            return null;
        }
    }


    @Transactional
    public void roomStatusUpdate(RoomOrderDTO roomOrderDTO) {
        roomRepository.roomStatusUpdate(roomOrderDTO);
    }

    @Transactional
    public void roomStatusUpdate1(RoomOrderDTO roomOrderDTO) {
        roomRepository.roomStatusUpdate1(roomOrderDTO);
    }

    @Transactional
    public void roomStatusUpdate2(Long roomIdx, Long roomorderIdx) {
        roomRepository.roomStatusUpdate2(roomIdx, roomorderIdx);
    }

    @Transactional
    public void roomCheckOut(Long roomIdx) {
        roomRepository.roomCheckOut(roomIdx);
    }

    @Transactional
    public void roomCheckOutEmpty(Long roomIdx) {
        roomRepository.roomCheckOutEmpty(roomIdx);
    }




    @Transactional
    public void roomPriceUpdate(int roomPrice, String roomType, Long storeIdx) {
        roomRepository.roomPriceUpdate(roomPrice, roomType, storeIdx);
    }



    public List<RoomDTO> roomTypeSearch(Long storeIdx) {


        List<Room> rooms = roomRepository.roomTypeSearch(storeIdx);


        List<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());

        return roomDTOS;
    }


    public List<RoomDTO> storeroomTypeSearch(Long storeIdx) {


        List<Room> rooms = roomRepository.storeroomTypeSearch(storeIdx);


        List<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());

        return roomDTOS;
    }



    public RoomDTO roomTypeSearchToTypeString(Long storeIdx,String roomType) {


        Optional<Room> room = roomRepository.roomTypeSearchToTypeString(storeIdx,roomType);


        RoomDTO roomDTO = modelMapper.map(room, RoomDTO.class);

        return roomDTO;
    }


    //특정 객실의 정보 찾기
    public RoomDTO roomTypeSearchOne(Long storeIdx,String roomType){

        Optional<Room> room = roomRepository.roomTypeSearchOne(storeIdx,roomType);

        return modelMapper.map(room,RoomDTO.class);
    }





}