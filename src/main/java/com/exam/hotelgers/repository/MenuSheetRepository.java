package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.MenuSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MenuSheetRepository extends JpaRepository<MenuSheet,Long> {

    List<MenuSheet> findByMenuOrder_MemberIdx(Long memberIdx);



    //메뉴오더 id로 주문메뉴 찾기
    @Query("select m from MenuSheet m where m.menuOrder.menuorderIdx = :menuOrderIdx")
    List<MenuSheet> findByMenuOrderMenu(Long menuOrderIdx);





}
