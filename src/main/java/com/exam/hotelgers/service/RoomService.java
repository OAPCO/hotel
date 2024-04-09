package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.AppData;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    //등록
    //삽입
    public Room register(RoomDTO roomDTO) {
        //현재 매장번호를 룸에 추가
        roomDTO.setStoreIdx(AppData.getStoreId());
        Room roomEntity = modelMapper.map(roomDTO, Room.class);
        Room result = roomRepository.save(roomEntity);

        return result;
    }
    //수정
    public RoomDTO modify(RoomDTO roomDTO) {
        Optional<Room> search = roomRepository.findById(roomDTO.getRoomIdx());

        if(search.isPresent()) {
            Room roomEntity = modelMapper.map(roomDTO, Room.class);
            roomRepository.save(roomEntity);
        }
        RoomDTO result = search.map(data ->modelMapper.map(data, RoomDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long roomIdx) {
        roomRepository.deleteById(roomIdx);
    }

    //전체조회
    public Page<RoomDTO> list(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                Sort.by(Sort.Direction.DESC,"id"));

        //해당 매장에 속하는 룸만 읽어온다.
        Page<Room> roomEntities = roomRepository.findByStoreId(AppData.getStoreId(),pageable);


        Page<RoomDTO> result = roomEntities.map(data->modelMapper.map(data,RoomDTO.class));

        return result;
    }

    //개별조회
    public RoomDTO read(Long id) {
        Optional<Room> roomEntity = roomRepository.findById(id);
        //RoomDTO result = modelMapper.map(roomEntity, RoomDTO.class);
        RoomDTO result = roomEntity.map(data->modelMapper.map(data, RoomDTO.class)).orElse(null);

        return result;
    }
}
