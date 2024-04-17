package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.BranchChiefDTO;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.entity.BranchChief;
import com.exam.hotelgers.repository.BranchChiefRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchChiefService {

    private final BranchChiefRepository branchChiefRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    public Long register(BranchChiefDTO branchChiefDTO){

        Optional<BranchChief> branchChiefidCheck = branchChiefRepository.findByBranchChiefId(branchChiefDTO.getBranchChiefId());


        if(branchChiefidCheck.isPresent()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }

        String password = passwordEncoder.encode(branchChiefDTO.getPassword());
        BranchChief branchChief = modelMapper.map(branchChiefDTO, BranchChief.class);



        if(branchChiefDTO.getRoleType().equals(RoleType.BRANCHCHIEF)){
            branchChief.setRoleType(RoleType.BRANCHCHIEF);
        }

        branchChief.setPassword(password); //비밀번호는 암호화된 비밀번호로 변경처리

        Long branchChiefIdx = branchChiefRepository.save(branchChief).getBranchChiefIdx();

        return branchChiefIdx;
    }





    public void delete(Long branchChiefIdx){
        branchChiefRepository.deleteById(branchChiefIdx);
    }


    public BranchChiefDTO read(Long branchChiefIdx){

        Optional<BranchChief> temp= branchChiefRepository.findById(branchChiefIdx);


        return modelMapper.map(temp,BranchChiefDTO.class);
    }


    public Page<BranchChiefDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"branchChiefIdx"));

        Page<BranchChief> branchChiefs = branchChiefRepository.findAll(page);


        Page<BranchChiefDTO> BranchChiefDTOS = branchChiefs.map(data->modelMapper.map(data,BranchChiefDTO.class));

        return BranchChiefDTOS;
    }





}
