package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
//메소드별마다 지정가능, 클래스에다 지정가능
//일괄처리
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public Long register(MemberDTO memberDTO) {
        
        
        Optional<Member> memberEntity = memberRepository
                .findByMemberEmail(memberDTO.getMemberEmail());

        if(memberEntity.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String password = passwordEncoder.encode(memberDTO.getMemberPwd());
        Member member = modelMapper.map(memberDTO, Member.class);

        member.setMemberPwd(password);
        member.setRoleType(RoleType.USER);
        System.out.println(member.toString());
        memberRepository.save(member);

        return memberRepository.save(member).getMemberIdx();
    }


    public void modify(MemberDTO memberDTO){


        Optional<Member> memberEntity = memberRepository
                .findByMemberEmail(memberDTO.getMemberEmail());

        if(memberEntity.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String password = passwordEncoder.encode(memberDTO.getMemberPwd());
        Member member = modelMapper.map(memberDTO, Member.class);

        member.setMemberPwd(password);
        member.setRoleType(RoleType.USER);

        memberRepository.save(member);

    }

    public MemberDTO read(Long memberIdx){

        Optional<Member> member= memberRepository.findById(memberIdx);


        return modelMapper.map(member,MemberDTO.class);
    }
    


    public Page<MemberDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"memberIdx"));

        Page<Member> members = memberRepository.findAll(page);


        Page<MemberDTO> memberDTOS = members.map(data->modelMapper.map(data,MemberDTO.class));

        return memberDTOS;
    }



    public void delete(Long memberIdx){
        memberRepository.deleteById(memberIdx);
    }
}
