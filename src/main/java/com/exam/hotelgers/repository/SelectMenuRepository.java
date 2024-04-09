package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.SelectMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelectMenuRepository extends JpaRepository<SelectMenu,Long> {
    Optional<SelectMenu> findBySelectMenuIdx(String menuSheetNo);
}
