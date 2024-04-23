package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {



    //사용자 아이디로 조회
    Optional<Store> findByStoreCd(String storeCd);

    Optional<Store> findByStoreIdx(Long storeIdx);




    @Query("select s from Store s where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
    "and (:#{#searchDTO.storeName} is null or s.storeName LIKE %:#{#searchDTO.storeName}%)"+
    "and (:#{#searchDTO.storeGrade} is null or s.storeGrade = %:#{#searchDTO.storeGrade}%)"+
    "and (:#{#searchDTO.storeCd} is null or s.storeCd LIKE %:#{#searchDTO.storeCd}%)"+
    "and (:#{#searchDTO.storeChiefEmail} is null or s.storeChiefEmail LIKE %:#{#searchDTO.storeChiefEmail}%)"+
    "and (:#{#searchDTO.storeChief} is null or s.storeChief LIKE %:#{#searchDTO.storeChief}%)"+
    "and (:#{#searchDTO.brandName} is null or s.brand.brandName LIKE %:#{#searchDTO.brandName}%)"+
    "and (:#{#searchDTO.storeStatus} is null or s.storeStatus = %:#{#searchDTO.storeStatus}%)"+
    "and (:#{#searchDTO.storePType} is null or s.storePType = %:#{#searchDTO.storePType}%)"
    )
    Page<Store> multiSearch(SearchDTO searchDTO,
                            Pageable pageable);





}
