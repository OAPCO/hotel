package com.exam.hotelgers.service;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    //보안인증을 통한 로그인처리(.usernameParameter("userid") =>userid)
    @Override
    public UserDetails loadUserByUsername(String userid) {
        System.out.println("관리자 로그인");
        Optional<Admin> adminEntity = adminRepository.findByAdminId(userid);
        if (adminEntity.isPresent()) { //관리자에 존재하면
            //아이디, 비밀번호, 권한을 이용해서 로그인처리
            return User.withUsername(adminEntity.get().getAdminId())
                    .password(adminEntity.get().getPassword())
                    .roles(adminEntity.get().getRoleType().name())
                    .build();
        }
        //일반회원 및 관리자에 존재하지 않으면 오류발생(Console)
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}