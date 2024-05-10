package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //사용자 아이디로 조회
    Optional<Member> findByMemberEmail(String memberEmail);

    Optional<Member> findByMemberIdx(Long memberIdx);


    
    //로그인중인 회원 본인 정보 조회
    @Query("select m from Member m where (m.memberEmail LIKE %:userId%)")
    Optional<Member> memberInfoSearch(String userId);



    //회원 정보 수정
    @Modifying
    @Query("update Member m set " +
            "m.memberEmail = :#{#searchDTO.memberEmail}, " +
            "m.memberName = :#{#searchDTO.memberName} " +
            "where m.memberIdx = :#{#searchDTO.memberIdx}")
    void memberInfoUpdate(SearchDTO searchDTO);


    //회원 탈퇴
    @Modifying
    @Query("delete from Member m where m.memberIdx = :#{#searchDTO.memberIdx}")
    void memberInfoDelete(SearchDTO searchDTO);


    //비밀번호 체크 쿼리 - 사용처 : 탈퇴 전 본인확인
    @Query("select m from Member m where (m.memberId LIKE %:userId%)")
    Optional<Member> memberPwdCheck(SearchDTO searchDTO);
}
