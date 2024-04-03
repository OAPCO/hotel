package com.exam.hotelgers.service;
//로그인만 처리

import com.exam.hotelgers.entity.Member;
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

    @Override
    public UserDetails loadUserByUsername(String memberEmail) {
        //가져온 아이디로 존재여부를 검색
        Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);
        //관리자
        //지점

        if(member.isPresent()) { //입력한 아이디가 존재하면

            log.info("로그인 시도 아이디 : "+member.get().getMemberEmail());
            log.info("로그인 시도 비밀번호 : "+member.get().getMemberPwd());
            log.info("로그인 시도 권한 : "+member.get().getRoleType().name());

            return User.withUsername(member.get().getMemberEmail()) //조회한 아이디
                    .password(member.get().getMemberPwd())      //조회한 비밀번호
                    .roles(member.get().getRoleType().name())  //조회한 권한
                    .build();

        } else {
            throw new UsernameNotFoundException("가입되지 않은 아이디 :"+memberEmail);
        }
    }

}
