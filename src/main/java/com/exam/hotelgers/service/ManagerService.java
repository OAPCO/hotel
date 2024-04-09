package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.ManagerDTO;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.repository.ManagerRepository;
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
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    public Long register(ManagerDTO managerDTO){

        Optional<Manager> manageridCheck = managerRepository.findByManagerId(managerDTO.getManagerId());


        if(manageridCheck.isPresent()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }

        String password = passwordEncoder.encode(managerDTO.getPassword());
        Manager manager = modelMapper.map(managerDTO, Manager.class);




        if(managerDTO.getRoleType().equals(RoleType.MANAGER)){
            manager.setRoleType(RoleType.MANAGER);
        }

        manager.setPassword(password); //비밀번호는 암호화된 비밀번호로 변경처리

        Long managerIdx = managerRepository.save(manager).getManagerIdx();

        return managerIdx;
    }





    public void delete(Long managerIdx){
        managerRepository.deleteById(managerIdx);
    }


    public ManagerDTO read(Long managerIdx){

        Optional<Manager> temp= managerRepository.findById(managerIdx);


        return modelMapper.map(temp,ManagerDTO.class);
    }


    public Page<ManagerDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"managerIdx"));

        Page<Manager> managers = managerRepository.findAll(page);


        Page<ManagerDTO> ManagerDTOS = managers.map(data->modelMapper.map(data,ManagerDTO.class));

        return ManagerDTOS;
    }


}
