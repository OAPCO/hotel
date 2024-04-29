package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.MenuCateDTO;

import com.exam.hotelgers.entity.MenuCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MenuCateRepository extends JpaRepository<MenuCate,Long> {
    Optional<MenuCate> findByMenuCateName(String menuCateName);
}
