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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    public Long register(AdminDTO adminDTO){

        Optional<Admin> adminidCheck = adminRepository.findByAdminId(adminDTO.getAdminId());


        if(adminidCheck.isPresent()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }

        String password = passwordEncoder.encode(adminDTO.getAdminPwd());
        Admin admin = modelMapper.map(adminDTO, Admin.class);


        //어드민레벨이 1일 경우 매니저
        if(adminDTO.getAdminLevel() == 1){
            admin.setRoleType(RoleType.MANAGER);
        }

        if(adminDTO.getAdminLevel() == 2){
            admin.setRoleType(RoleType.ADMIN);
        }

        admin.setAdminPwd(password); //비밀번호는 암호화된 비밀번호로 변경처리

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

        Page<Admin> admins = adminRepository.findAll(page);


        Page<AdminDTO> AdminDTOS = admins.map(data->modelMapper.map(data,AdminDTO.class));

        return AdminDTOS;
    }


}
