package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna,Long> {

    @Query("select a from Qna a where " +
            "(:#{#searchDTO.qnaTitle} is null or a.qnaTitle LIKE %:#{#searchDTO.qnaTitle}%) " +
            "and (:#{#searchDTO.qnaContent} is null or a.qnaContent LIKE %:#{#searchDTO.qnaContent}%) "+
            "and (:#{#searchDTO.qnaStatus} is null or a.qnaStatus = %:#{#searchDTO.qnaStatus}%) ")
    Page<Qna> selectQna (Pageable pageable,SearchDTO searchDTO);


    @Modifying
    @Query("update Qna q set " +
            "q.qnaAnswer = :#{#searchDTO.qnaAnswer}," +
            "q.qnaStatus = 1 " +
            "where q.qnaIdx = :#{#searchDTO.qnaIdx}")
    void answerUpdate(SearchDTO searchDTO);



    @Query("select a from Qna a where a.memberIdx = :memberIdx")
    Page<Qna> selectMyQna (Pageable pageable,Long memberIdx);

}
