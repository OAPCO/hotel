package com.exam.hotelgers.service;
import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log
public class AdminLoginService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) {

        Optional<Admin> adminEntity = adminRepository.findByAdminId(userid);

        if (adminEntity.isPresent()) {

            log.info("어드민 로그인서비스 들어옴");

            return User.withUsername(adminEntity.get().getAdminId())
                    .password(adminEntity.get().getPassword())
                    .roles(adminEntity.get().getRoleType().name())
                    .build();
        }
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }

}