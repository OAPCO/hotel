package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;


    public Long register(AdminDTO adminDTO){


        Admin admin = modelMapper.map(adminDTO, Admin.class);


        //어드민레벨이 1일 경우 매니저
        if(adminDTO.getAdminLevel() == 1){
            admin.setRoleType(RoleType.MANAGER);
        }

        if(adminDTO.getAdminLevel() == 2){
            admin.setRoleType(RoleType.ADMIN);
        }

        Long adminIdx = adminRepository.save(admin).getAdminIdx();

        return adminIdx;
    }





    public void delete(Long adminIdx){
        adminRepository.deleteById(adminIdx);
    }


    public AdminDTO read(Long adminIdx){

        Optional<Admin> temp= adminRepository.findById(adminIdx);


        return modelMapper.map(temp,AdminDTO.class);
    }


    public Page<AdminDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"adminIdx"));

        Page<Admin> adminEntities = adminRepository.findAll(page);


        Page<AdminDTO> AdminDTOS = adminEntities.map(data->modelMapper.map(data,AdminDTO.class));

        return AdminDTOS;
    }


}
