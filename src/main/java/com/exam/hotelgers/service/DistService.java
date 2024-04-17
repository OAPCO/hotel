package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.DistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
public class DistService {

    private final DistRepository distRepository;
    private final ModelMapper modelMapper;


    public Long register(DistDTO distDTO) {


        Optional<Dist> distEntity = distRepository
                .findByDistCd(distDTO.getDistCd());

        if(distEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }

        Dist dist = modelMapper.map(distDTO, Dist.class);

        distRepository.save(dist);

        return distRepository.save(dist).getDistIdx();
    }


    public void modify(DistDTO distDTO){


        Optional<Dist> temp = distRepository
                .findByDistIdx(distDTO.getDistIdx());

        if(temp.isPresent()) {

            Dist dist = modelMapper.map(distDTO, Dist.class);
            distRepository.save(dist);
        }

    }

    public DistDTO read(Long distIdx){

        Optional<Dist> dist= distRepository.findById(distIdx);


        return modelMapper.map(dist, DistDTO.class);
    }



    public Page<DistDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"distIdx"));

        Page<Dist> dists = distRepository.findAll(page);


        Page<DistDTO> distDTOS = dists.map(data->modelMapper.map(data, DistDTO.class));

        return distDTOS;
    }





    public void delete(Long distIdx){
        distRepository.deleteById(distIdx);
    }

    public Page<DistDTO> searchadminstoredistmange(String distName, String distChief, Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distIdx"));

        Page<Dist> dists = distRepository.multiSearchadminsdm(distName, distChief, page);
        return dists.map(this::convertToDistDTO);
    }

    private DistDTO convertToDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }

    public Page<DistDTO> searchadmindr(String distChief, Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distIdx"));

        Page<Dist> dists = distRepository.multiSearchadmdr(distChief, page);
        return dists.map(this::convertToDistDTO);
    }
}
