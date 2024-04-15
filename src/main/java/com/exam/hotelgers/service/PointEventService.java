package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.PointEventDTO;
import com.exam.hotelgers.entity.PointEvent;
import com.exam.hotelgers.repository.PointEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PointEventService {
    private final PointEventRepository pointEventRepository;
    private final ModelMapper modelMapper;
    
    public PointEvent insert(PointEventDTO pointEventDTO) {
        PointEvent pointEvent = modelMapper.map(pointEventDTO, PointEvent.class);
        PointEvent result = pointEventRepository.save(pointEvent);

        return result;
    }

    //수정
    public PointEventDTO update(PointEventDTO pointEventDTO) {
        Optional<PointEvent> search = pointEventRepository.findById(pointEventDTO.getPointEventIdx());

        if(search.isPresent()) {
            PointEvent pointEvent = modelMapper.map(pointEventDTO, PointEvent.class);
            pointEventRepository.save(pointEvent);
        }
        PointEventDTO result = search.map(data ->modelMapper.map(data, PointEventDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long id) {
        pointEventRepository.deleteById(id);
    }

    //전체조회
    public Page<PointEventDTO> select(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                Sort.by(Sort.Direction.DESC,"pointEventIdx"));

        Page<PointEvent> memberEntities = pointEventRepository.findAll(pageable);


        Page<PointEventDTO> result = memberEntities.map(data->modelMapper.map(data,PointEventDTO.class));

        return result;
    }

    //개별조회
    public PointEventDTO read(Long id) {
        Optional<PointEvent> pointEvent = pointEventRepository.findById(id);
        //PointEventDTO result = modelMapper.map(pointEvent, PointEventDTO.class);
        PointEventDTO result = pointEvent.map(data->modelMapper.map(data, PointEventDTO.class)).orElse(null);

        return result;
    }
}
