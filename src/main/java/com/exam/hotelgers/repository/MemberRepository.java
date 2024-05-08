package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //사용자 아이디로 조회
    Optional<Member> findByMemberEmail(String memberEmail);


    Optional<Member> findByMemberIdx(Long memberIdx);

    
    //회원 본인 정보 조회
    @Query("select m from Member m where (m.memberId LIKE %:userId%)")
    Optional<Member> memberInfoSearch(String userId);

}
