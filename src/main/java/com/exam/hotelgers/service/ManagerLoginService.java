package com.exam.hotelgers.service;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.repository.ManagerRepository;
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
public class ManagerLoginService implements UserDetailsService {
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) {
        System.out.println("관리자 로그인");
        Optional<Manager> managerEntity = managerRepository.findByManagerId(userid);
        if (managerEntity.isPresent()) {
            System.out.println("관리자 로그인");

            return User.withUsername(managerEntity.get().getManagerId())
                    .password(managerEntity.get().getPassword())
                    .roles(managerEntity.get().getRoleType().name())
                    .build();

        }
        //일반회원 및 관리자에 존재하지 않으면 오류발생(Console)
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}