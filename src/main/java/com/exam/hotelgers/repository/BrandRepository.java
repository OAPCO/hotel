package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {


    Optional<Brand> findByBrandIdx(Long brandIdx);
    Optional<Brand> findByBrandCd(String brandCd);

    Optional<Brand> findByBrandName(String BrandName);

    @Query("select b from Brand b join b.storeList s where (:distName is null or b.dist.distName LIKE %:distName%)"+
            "and (:distChiefEmail is null or b.dist.distChiefEmail LIKE %:distChiefEmail%)"+
            "AND (:brandName IS NULL OR b.brandName LIKE %:brandName%) " +
            "AND (:brandCd IS NULL OR b.brandCd LIKE %:brandCd%)"+
            "AND (:StoreStatus IS NULL OR s.storeStatus = :StoreStatus)"
    )
    Page<Brand> multisearch(
            @Param("distChiefEmail") String distChiefEmail,
            @Param("distName") String distName,
            @Param("brandName") String brandName,
            @Param("brandCd") String brandCd,
            @Param("StoreStatus") StoreStatus storestatus,
            Pageable pageable
    );


}
