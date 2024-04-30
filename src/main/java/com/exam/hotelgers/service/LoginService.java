package com.exam.hotelgers.service;

import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByMemberEmail(username);
        Optional<Admin> admin = adminRepository.findByAdminId(username);

        if (member.isPresent()) { // 회원 로그인
            Member foundMember = member.get();
            log.info("회원 로그인 시도 아이디 : " + foundMember.getMemberEmail());
            log.info("회원 로그인 시도 비밀번호 : " + foundMember.getPassword());
            log.info("회원 로그인 시도 권한 : " + foundMember.getRoleType().name());

            return User.withUsername(foundMember.getMemberEmail())
                    .password(foundMember.getPassword())
                    .roles(foundMember.getRoleType().name())
                    .build();
        } else if (admin.isPresent()) { // 어드민 로그인
            Admin foundAdmin = admin.get();
            log.info("어드민 로그인 시도 아이디: " + foundAdmin.getAdminId());
            log.info("어드민 로그인 시도 비밀번호: " + foundAdmin.getPassword());
            log.info("어드민 로그인 시도 권한: " + foundAdmin.getRoleType());

            return User.withUsername(foundAdmin.getAdminId())
                    .password(foundAdmin.getPassword())
                    .roles(foundAdmin.getRoleType().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("가입되지 않은 아이디: " + username);
        }
    }
}