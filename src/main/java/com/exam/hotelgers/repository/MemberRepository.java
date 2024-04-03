package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //사용자 아이디로 조회
    Optional<Member> findByMemberEmail(String memberEmail);
}
