package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.MemberDTO;
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





    //회원 탈퇴 처리부분

    //암호화된 비밀번호를 먼저 찾기 위한 쿼리.
    @Query("select m.password from Member m where m.memberIdx = :#{#searchDTO.memberIdx}")
    String memberPwdFind(SearchDTO searchDTO);

    //필요x?
    @Query("select m.memberIdx from Member m where (m.password LIKE :passwordEnc) " +
            "and m.memberIdx = :#{#searchDTO.memberIdx}")
    Long memberPwdCheck(SearchDTO searchDTO,String passwordEnc);

    //회원 탈퇴
    @Modifying
    @Query("delete from Member m where m.memberIdx = :memberIdx")
    void memberInfoDelete(Long memberIdx);

    @Query("select m.memberEmail from Member m where m.memberEmail = :#{#searchDTO.memberEmail}")
    String emailcheck(SearchDTO searchDTO);

}
