package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.MenuCateDTO;

import com.exam.hotelgers.entity.MenuCate;
import com.exam.hotelgers.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;
@Repository
public interface MenuCateRepository extends JpaRepository<MenuCate,Long> {
    Optional<MenuCate> findByMenuCateName(String menuCateName);


    //현재 가진 매장의 메뉴카테 불러오기
    @Query("select m from MenuCate m where m.store.storeIdx = :storeIdx")
    List<MenuCate> loginManagerMenuCateSearch(Long storeIdx);
}
