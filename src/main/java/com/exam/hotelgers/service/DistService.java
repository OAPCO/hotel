package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.distDTO;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.DistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
public class DistService {

    private final DistRepository distRepository;
    private final ModelMapper modelMapper;


    public Long register(distDTO distDTO) {


        Optional<Dist> distEntity = distRepository
                .findByDistCd(distDTO.getDistCd());

        if(distEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }

        Dist dist = modelMapper.map(distDTO, Dist.class);

        distRepository.save(dist);

        return distRepository.save(dist).getDistIdx();
    }


    public void modify(distDTO distDTO){


        Optional<Dist> temp = distRepository
                .findByDistIdx(distDTO.getDistIdx());

        if(temp.isPresent()) {

            Dist dist = modelMapper.map(distDTO, Dist.class);
            distRepository.save(dist);
        }

    }

    public distDTO read(Long distIdx){

        Optional<Dist> dist= distRepository.findById(distIdx);


        return modelMapper.map(dist,distDTO.class);
    }



    public Page<distDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"distIdx"));

        Page<Dist> dists = distRepository.findAll(page);


        Page<distDTO> distDTOS = dists.map(data->modelMapper.map(data,distDTO.class));

        return distDTOS;
    }



    public void delete(Long distIdx){
        distRepository.deleteById(distIdx);
    }
}
