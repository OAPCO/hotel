package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.MenuCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DetailmenuRepository extends JpaRepository<Detailmenu,Long> {


    Optional<Detailmenu> findByDetailmenuIdx(Long detailmenuIdx);

    List<Detailmenu> findByMenuCateMenuCateIdx(Long menuCateIdx);


    //현재 가진 매장의 디테일 메뉴
    @Query("select d from Detailmenu d where d.store.storeIdx = :storeIdx")
    List<Detailmenu> loginManagerdetailMenuCateSearch(Long storeIdx);

}
