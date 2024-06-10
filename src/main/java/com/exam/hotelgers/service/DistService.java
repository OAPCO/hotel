package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.security.Principal;
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
    private final SearchService searchService;
    private final DistChiefRepository distChiefRepository;



    private DistDTO convertToDTO(Dist dist) {
        DistDTO dto = modelMapper.map(dist, DistDTO.class);
        dto.setStoreDTOList(searchService.convertToStoreDTOList(dist.getStoreList()));
        dto.setDistChiefDTO(searchService.convertToDistChiefDTO(dist.getDistChief()));
        return dto;
    }

    public Long register(DistDTO distDTO) {
        // DistDTO로부터 총판장의 고유 식별자(distChiefIdx)를 가져옵니다.
        Long distChiefIdx = distDTO.getDistChiefDTO().getDistChiefIdx();

        // 총판장의 고유 식별자가 null인 경우 예외를 던집니다.
        if (distChiefIdx == null) {
            throw new IllegalArgumentException("총판장 고유 식별자가 유효하지 않습니다.");
        }

        // 총판장 고유 식별자로 총판장을 검색합니다.
        Optional<DistChief> distChiefOptional = distChiefRepository.findById(distChiefIdx);

        // 총판장이 존재하지 않는 경우 예외를 던집니다.
        if (!distChiefOptional.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판장입니다.");
        }

        // DistCd를 이용하여 이미 생성된 총판인지 확인합니다.
        Optional<Dist> distEntity = distRepository.findByDistCd(distDTO.getDistCd());
        // 이미 생성된 총판인 경우 예외를 던집니다.
        if(distEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }

        // DistDTO를 Dist 엔티티로 변환합니다.
        Dist dist = modelMapper.map(distDTO, Dist.class);

        // 총판 엔티티에 총판장 정보를 설정합니다.
        dist.setDistChief(distChiefOptional.get());
        dist.setStoreCount(0);

        // 총판을 저장하고 새로운 총판의 ID를 반환합니다.
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


        return dists.map(this::convertToDTO);
    }



    public void delete(Long distIdx){
        distRepository.deleteById(distIdx);
    }





    private DistDTO convertToDistDTO(Dist dist) {

        DistDTO dto = modelMapper.map(dist, DistDTO.class); //어떤것이든 고정
        
        dto.setDistChiefDTO(searchService.convertToDistChiefDTO(dist.getDistChief()));
        return dto;
    }


    
    //총판장이 가진 총판 찾기
    public List<DistDTO> distSearchforUserId(Principal principal) {


        List<Dist> dists = distRepository.distSearchforUserId(principal.getName());


        List<DistDTO> distDTOS = dists.stream()
                .map(dist -> modelMapper.map(dist, DistDTO.class))
                .collect(Collectors.toList());

        return distDTOS;
    }


    
    //매장수 증가
    @Transactional
    public void distStoreCountAdd(Long distIdx) {
        distRepository.distStoreCountAdd(distIdx);
    }


}
