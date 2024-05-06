package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("select b from Brand b where b.brandCd LIKE %:#{#searchDTO.brandCd}%")
    Optional<Brand> brandCheckGet(SearchDTO searchDTO);


    Optional<Brand> findByBrandIdx(Long brandIdx);
    Optional<Brand> findByBrandCd(String brandCd);

    Optional<Brand> findByBrandName(String BrandName);

    //수정전
//    @Query("select b from Brand b join b.storeList s where (:distName is null or b.dist.distName LIKE %:distName%)"+
//            "AND (:brandName IS NULL OR b.brandName LIKE %:brandName%) " +
//            "AND (:brandCd IS NULL OR b.brandCd LIKE %:brandCd%)"
//    )
    @Query("select b from Brand b join Store s where (:distName is null or b.distCd LIKE %:distName%)"+
            "AND (:brandName IS NULL OR b.brandName LIKE %:brandName%) " +
            "AND (:brandCd IS NULL OR b.brandCd LIKE %:brandCd%)"
    )
    Page<Brand> multisearch(
            @Param("distName") String distName,
            @Param("brandName") String brandName,
            @Param("brandCd") String brandCd,
            Pageable pageable
    );



    @Query("select b from Brand b where (:#{#searchDTO.distName} is null or b.distCd LIKE %:#{#searchDTO.distName}%)")
    List<Brand> distOfBrand(SearchDTO searchDTO);






}
