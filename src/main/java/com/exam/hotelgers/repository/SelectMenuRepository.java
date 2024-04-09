package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.SelectMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SelectMenuRepository extends JpaRepository<SelectMenu,Long> {
    Optional<SelectMenu> findBySelectMenuIdx(Long selectMenuIdx);
}
