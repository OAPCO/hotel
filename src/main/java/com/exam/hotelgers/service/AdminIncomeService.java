package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.AdminIncomeDTO;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.AdminIncomeRepository;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminIncomeService {

    private final AdminIncomeRepository adminIncomeRepository;
    private final ModelMapper modelMapper;


    public Long register(AdminIncomeDTO adminIncomeDTO) {


        AdminIncome adminIncome = modelMapper.map(adminIncomeDTO, AdminIncome.class);

        adminIncomeRepository.save(adminIncome);

        return adminIncomeRepository.save(adminIncome).getAdminIncomeIdx();
    }


    @Transactional
    public void incomePriceHalfModify(Long paymentIdx) {
        adminIncomeRepository.incomePriceHalfModify(paymentIdx);
    }




}