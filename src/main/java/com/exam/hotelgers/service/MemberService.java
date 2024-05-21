package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Reward;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.CouponRepository;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.RewardRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    private final RewardRepository rewardRepository;
    private final CouponRepository couponRepository;


    //이 부분은 회원 crud 부분


    public Long register(MemberDTO memberDTO) {


        Optional<Member> memberEntity = memberRepository
                .findByMemberEmail(memberDTO.getMemberEmail());

        if (memberEntity.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String password = passwordEncoder.encode(memberDTO.getPassword());
        Member member = modelMapper.map(memberDTO, Member.class);

        member.setPassword(password);
        member.setRoleType(RoleType.USER);
        memberRepository.save(member);

        return memberRepository.save(member).getMemberIdx();
    }


    public void modify(MemberDTO memberDTO) {


        Optional<Member> memberEntity = memberRepository
                .findByMemberIdx(memberDTO.getMemberIdx());

        if (memberEntity.isPresent()) {

            String password = passwordEncoder.encode(memberDTO.getPassword());
            Member member = modelMapper.map(memberDTO, Member.class);


            member.setPassword(password);
            member.setRoleType(RoleType.USER);

            memberRepository.save(member);
        }


    }

    public MemberDTO read(Long memberIdx) {

        Optional<Member> member = memberRepository.findById(memberIdx);


        return modelMapper.map(member, MemberDTO.class);
    }


    public Page<MemberDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "memberIdx"));

        Page<Member> members = memberRepository.findAll(page);


        Page<MemberDTO> memberDTOS = members.map(data -> modelMapper.map(data, MemberDTO.class));

        return memberDTOS;
    }


    public void delete(Long memberIdx) {
        memberRepository.deleteById(memberIdx);
    }


    //여기서부터 실제 회원 사이트의 서비스들


    //마이페이지 - 내 정보 조회
    public MemberDTO memberInfoSearch(Principal principal) {

        String userId = principal.getName();
        Optional<Member> member = memberRepository.memberInfoSearch(userId);

        if (!member.isPresent()) {
            throw new IllegalStateException("없는 회원입니다.");
        }


        return modelMapper.map(member.get(), MemberDTO.class);
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
        boolean matches = passwordEncoder.matches(passwordEnc, password);

        log.info("비밀번호 대조 결과@@" + matches);


//        Long memberIdx = memberRepository.memberPwdCheck(searchDTO,passwordEnc);

        if (matches == false) {
            throw new IllegalStateException("비밀번호가 다릅니다.");
        }

        if (matches == true) {
            memberRepository.memberInfoDelete(searchDTO.getMemberIdx());
        }


    }

    public MemberDTO memberPointSearch(Principal principal) {
        String memberEmail = principal.getName();
        List<Reward> rewardList = rewardRepository.findByMemberEmail(memberEmail);
        List<Coupon> couponList = couponRepository.findByMemberEmail(memberEmail);

        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        memberDTO.setRewardList(rewardList);
        memberDTO.setCouponList(couponList);

        return memberDTO;
    }

    public int checkEmailDuplication(SearchDTO searchDTO) {
        // 입력된 이메일과 중복된 이메일을 찾기 위해 리포지토리에서 확인
        String existingEmail = memberRepository.emailcheck(searchDTO);

        // 중복된 이메일이 없으면 0 반환
        if (existingEmail == null) {
            log.info("중복이메일x");
            return 0;
        } else {
            // 중복된 이메일이 있으면 1 반환
            log.info("중복이메일o");
            return 1;
        }

    }

    public String kakaoregister(String userInfo, MemberDTO memberDTO) throws Exception {
        Map<String, Object> userInfoMap = objectMapper.readValue(userInfo, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) userInfoMap.get("kakao_account");
        Map<String, Object> propertiesMap = (Map<String, Object>) userInfoMap.get("properties");
        log.info("들어온 카카오 소셜 회원 정보: " + userInfo);
        String memberEmail = kakaoAccountMap.get("email").toString();
        String memberNickname = propertiesMap.get("nickname").toString();
        Character memberJoinType = 'k'; // 카카오 소셜 회원가입인 경우를 나타내는 플래그
        String randomPassword = generateRandomPassword(10);
        memberDTO.setMemberEmail(memberEmail);
        memberDTO.setMemberName(memberNickname);
        memberDTO.setMemberJoinType(memberJoinType);
        memberDTO.setPassword(randomPassword); // 랜덤 패스워드 설정
        memberDTO.setKakaopassword(randomPassword); // 랜덤 패스워드 설정
        Optional<Member> memberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (memberEntity.isPresent()) {
            log.info ("이미 가입된 카카오 계정이므로 로그인으로.");
            return kakaologin(userInfo, memberDTO); //여기는 비번안넘어감
        }
        Member member = modelMapper.map(memberDTO, Member.class);

        String password = passwordEncoder.encode(randomPassword);

        member.setRoleType(RoleType.USER);
        member.setPassword(password);

        memberRepository.save(member);
        log.info(memberEmail + password+" 카카오 소셜 가입완료");
        return memberEmail + ":" + randomPassword;
    }


    private String generateRandomPassword(int length) { //패스워드 랜덤주입 (소셜전용)
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }
    public String kakaologin(String userInfo,MemberDTO memberDTO) throws Exception {


        // 받은 JSON 데이터를 Map 형식으로 변환합니다.
        Map<String, Object> userInfoMap = objectMapper.readValue(userInfo, new TypeReference<Map<String, Object>>() {});

        // kakao_account와 properties 값을 다시 Map으로 변환합니다.
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) userInfoMap.get("kakao_account");
        Map<String, Object> propertiesMap = (Map<String, Object>) userInfoMap.get("properties");

        log.info("들어온 카카오 로그인: " + userInfo);

        // 필요한 필드를 추출하여 새로운 객체에 담습니다.
        String userid = kakaoAccountMap.get("email").toString();
        String password= memberRepository.kakaopassword(userid);

//      암호화해제 줄 하나
        log.info("id:   "+userid+"    패스워드     "+password);


        return userid + ":" + password;
    }


}
