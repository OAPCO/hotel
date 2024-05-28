package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {


    //notice/list 검색
    @Query("select n from Notice n where " +
            "(:#{#searchDTO.noticeTitle} is null or n.noticeTitle LIKE %:#{#searchDTO.noticeTitle}%) " +
            "and (:#{#searchDTO.noticeContent} is null or n.noticeContent LIKE %:#{#searchDTO.noticeContent}%) " +
            "and (:#{#searchDTO.noticeType} is null or n.noticeType LIKE %:#{#searchDTO.noticeType}%)")
    Page<Notice> selectNotice (Pageable pageable, SearchDTO searchDTO);




    @Query("select n from Notice n where n.noticeType LIKE 'notice'")
    List<Notice> findNoticeAll();


    @Query("select n from Notice n where n.noticeIdx = :noticeIdx")
    Optional<Notice> findNoticeOne(Long noticeIdx);




}
