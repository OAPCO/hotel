package com.exam.hotelgers.service;
//로그인만 처리

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import com.exam.hotelgers.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    //보안인증을 통한 로그인처리
    @Override
    public UserDetails loadUserByUsername(String userid) {
        //사용자 로그인 확인
        Optional<Member> memberEntity = memberRepository.findByMemberEmail(userid);
        if (memberEntity.isPresent()) {
            return User.withUsername(memberEntity.get().getMemberEmail())
                    .password(memberEntity.get().getPassword())
                    .roles(memberEntity.get().getRoleType().name())
                    .build();
        }

        //관리자 로그인 확인
        Optional<Admin> adminEntity = adminRepository.findByAdminId(userid);
        if (adminEntity.isPresent()) {
            System.out.println("관리자 로그인");

                return User.withUsername(adminEntity.get().getAdminId())
                        .password(adminEntity.get().getPassword())
                        .roles(adminEntity.get().getRoleType().name())
                        .build();

        }


        //매니저 로그인 확인
        Optional<Manager> managerEntity = managerRepository.findByManagerId(userid);
        if (managerEntity.isPresent()) {
            System.out.println("관리자 로그인");

            return User.withUsername(managerEntity.get().getManagerId())
                    .password(managerEntity.get().getPassword())
                    .roles(managerEntity.get().getRoleType().name())
                    .build();

        }

        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}
