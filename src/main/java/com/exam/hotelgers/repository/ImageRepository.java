package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query(value = "select i from Image i where i.banner.bannerIdx = :bannerIdx order by i.imageIdx")
    List<Image> find(Long bannerIdx);


    List<Image> findByBanner_BannerIdxOrderByImageIdxAsc(Long bannerIdx);

    @Query(value = "select i  from Image i where i.banner.bannerIdx = : bannerIdx")
    List<Image> findfind(Long bannerIdx, Pageable pageable);

    Page<Image> findByBanner_BannerIdx(Long bannerIdx, Pageable pageable);

    Long deleteImageByImageIdx(Long imageIdx);


}
