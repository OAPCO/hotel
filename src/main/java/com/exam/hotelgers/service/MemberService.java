package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    
    
    //이 부분은 회원 crud 부분


    public Long register(MemberDTO memberDTO) {
        
        
        Optional<Member> memberEntity = memberRepository
                .findByMemberEmail(memberDTO.getMemberEmail());

        if(memberEntity.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String password = passwordEncoder.encode(memberDTO.getPassword());
        Member member = modelMapper.map(memberDTO, Member.class);

        member.setPassword(password);
        member.setRoleType(RoleType.USER);
        memberRepository.save(member);

        return memberRepository.save(member).getMemberIdx();
    }


    public void modify(MemberDTO memberDTO){


        Optional<Member> memberEntity = memberRepository
                .findByMemberIdx(memberDTO.getMemberIdx());

        if(memberEntity.isPresent()) {

            String password = passwordEncoder.encode(memberDTO.getPassword());
            Member member = modelMapper.map(memberDTO, Member.class);


            member.setPassword(password);
            member.setRoleType(RoleType.USER);

            memberRepository.save(member);
        }





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





    //여기서부터 실제 회원 사이트의 서비스들


    
    //마이페이지 - 내 정보 조회
    public MemberDTO memberInfoSearch(Principal principal) {

        String userId = principal.getName();
        Optional<Member> member = memberRepository.memberInfoSearch(userId);

        if(!member.isPresent()) {
            throw new IllegalStateException("없는 회원입니다.");
        }


        return modelMapper.map(member.get(),MemberDTO.class);
    }



    @Transactional
    //회원 정보수정
    public void memberInfoUpdate(SearchDTO searchDTO) {

        memberRepository.memberInfoUpdate(searchDTO);

    }



//    @Transactional
//    //회원탈퇴
//    public void memberInfoDelete(SearchDTO searchDTO) {
//
//
//        String password = passwordEncoder.encode(searchDTO.getPassword());
//        String passwordEnc = memberRepository.memberPwdFind(searchDTO);
//
//        Long memberIdx = memberRepository.memberPwdCheck(searchDTO,passwordEnc);
//
//        if (memberIdx==null) {
//            throw new IllegalStateException("비밀번호가 다릅니다.");
//        }
//
//
//        memberRepository.memberInfoDelete(memberIdx);
//
//    }


    @Transactional
    //회원탈퇴
    public void memberInfoDelete(SearchDTO searchDTO) {

        //암호화되어있는 비밀번호 찾기
        String passwordEnc = memberRepository.memberPwdFind(searchDTO);

        //사용자가 입력한 암호
        String password = searchDTO.getPassword();

        //두개가 일치하는지 비교
        boolean matches = passwordEncoder.matches(passwordEnc,password);

        log.info("비밀번호 대조 결과@@" + matches);


//        Long memberIdx = memberRepository.memberPwdCheck(searchDTO,passwordEnc);

        if (matches==false) {
            throw new IllegalStateException("비밀번호가 다릅니다.");
        }

        if (matches==true) {
            memberRepository.memberInfoDelete(searchDTO.getMemberIdx());
        }


    }


}
