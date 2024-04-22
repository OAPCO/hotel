package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.MenuCate;
import com.exam.hotelgers.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MenuoptionRepository  extends JpaRepository<MenuOption,Long> {
    Optional<MenuOption> findByMenuOptionIdx(Long menuOptionIdx);
}