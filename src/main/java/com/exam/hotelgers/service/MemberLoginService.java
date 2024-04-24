package com.exam.hotelgers.service;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.MemberRepository;
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
public class MemberLoginService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) {

        Optional<Member> memberEntity = memberRepository.findByMemberEmail(userid);
        if (memberEntity.isPresent()) {

            log.info("가져온거@@@@@@@@@@@   "+memberEntity.get().getMemberEmail());
            log.info("가져온거@@@@@@@@@@@   "+memberEntity.get().getPassword());

            return User.withUsername(memberEntity.get().getMemberEmail())
                    .password(memberEntity.get().getPassword())
                    .roles(memberEntity.get().getRoleType().name())
                    .build();
        }
        //일반회원 및 관리자에 존재하지 않으면 오류발생(Console)
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}