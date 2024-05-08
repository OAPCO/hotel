package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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



    public Page<Object> memberList(Pageable pageable) {

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.unsorted());

        List<DistChief> distChiefList = adminRepository.distChiefListSearch();
        List<Manager> managerList = adminRepository.managerListSearch();
        List<Member> memberList = adminRepository.memberListSearch();

        List<Object> allList = Stream.of(distChiefList, managerList, memberList)
                .flatMap(List::stream)
                .collect(Collectors.toList());


        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), allList.size());

        List<Object> subList = allList.subList(start, end);

        Page<Object> pageAllList = new PageImpl<>(subList, page, allList.size());

        return pageAllList;
    }




    public Page<Object> memberListSearch(Pageable pageable,SearchDTO searchDTO) {

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.unsorted());

        List<DistChief> distChiefList = adminRepository.distChiefListSearch1(searchDTO);
        List<Manager> managerList = adminRepository.managerListSearch1(searchDTO);
        List<Member> memberList = adminRepository.memberListSearch1(searchDTO);

        List<Object> allList = Stream.of(distChiefList, managerList, memberList)
                .flatMap(List::stream)
                .collect(Collectors.toList());


        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), allList.size());

        List<Object> subList = allList.subList(start, end);

        Page<Object> pageAllList = new PageImpl<>(subList, page, allList.size());

        return pageAllList;
    }




}
