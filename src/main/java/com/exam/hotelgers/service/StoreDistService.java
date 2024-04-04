package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.StoreDistDTO;
import com.exam.hotelgers.entity.StoreDist;
import com.exam.hotelgers.repository.StoreDistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
public class StoreDistService {

    private final StoreDistRepository storedistRepository;
    private final ModelMapper modelMapper;


    public Long register(StoreDistDTO storedistDTO) {
        
        
        Optional<StoreDist> storedistEntity = storedistRepository
                .findByStoreDistName(storedistDTO.getStoreDistName());

        if(storedistEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }

        StoreDist storedist = modelMapper.map(storedistDTO, StoreDist.class);

        storedistRepository.save(storedist);

        return storedistRepository.save(storedist).getStoreDistIdx();
    }


    public void modify(StoreDistDTO storedistDTO){


        Optional<StoreDist> storedistEntity = storedistRepository
                .findByStoreDistName(storedistDTO.getStoreDistName());

        if(storedistEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }

        StoreDist storedist = modelMapper.map(storedistDTO, StoreDist.class);


        storedistRepository.save(storedist);

    }

    public StoreDistDTO read(Long storeDistIdx){

        Optional<StoreDist> storedist= storedistRepository.findById(storeDistIdx);


        return modelMapper.map(storedist,StoreDistDTO.class);
    }
    


    public Page<StoreDistDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"storeDistIdx"));

        Page<StoreDist> storedists = storedistRepository.findAll(page);


        Page<StoreDistDTO> storedistDTOS = storedists.map(data->modelMapper.map(data,StoreDistDTO.class));

        return storedistDTOS;
    }



    public void delete(Long storeDistIdx){
        storedistRepository.deleteById(storeDistIdx);
    }
}
