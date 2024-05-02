package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminService {

    private final AdminRepository adminRepository;
    private final DistChiefRepository distChiefRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    public Long register(AdminDTO adminDTO){



        List<String> adminIdCheck = adminRepository.registerCheck(adminDTO.getAdminId());
        List<String> distChiefIdCheck = distChiefRepository.registerCheck(adminDTO.getAdminId());
        List<String> managerIdCheck = managerRepository.registerCheck(adminDTO.getAdminId());


        if(!adminIdCheck.isEmpty() || !distChiefIdCheck.isEmpty() || !managerIdCheck.isEmpty()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }



        String password = passwordEncoder.encode(adminDTO.getPassword());
        Admin admin = modelMapper.map(adminDTO, Admin.class);



        if(adminDTO.getRoleType().equals(RoleType.ADMIN)){
            admin.setRoleType(RoleType.ADMIN);
        }

        admin.setPassword(password); //비밀번호는 암호화된 비밀번호로 변경처리

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
