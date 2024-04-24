package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Detailmenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DetailmenuRepository extends JpaRepository<Detailmenu,Long> {
    Optional<Detailmenu> findByDetailMenuName(String detailMenuName);

    Optional<Detailmenu> findByDetailmenuIdx(Long detailmenuIdx);

    List<Detailmenu> findByMenuCateMenuCateIdx(Long menuCateIdx);

}
