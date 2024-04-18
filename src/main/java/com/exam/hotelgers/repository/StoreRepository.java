package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.search.Search;
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
public interface StoreRepository extends JpaRepository<Store, Long>, Search {



    //사용자 아이디로 조회
    Optional<Store> findByStoreCd(String storeCd);

    Optional<Store> findByStoreIdx(Long storeIdx);


//    @Query("SELECT s FROM Store s WHERE s.dist.distName like %:distName%")
//    Page<Store> distNameSearch(String distName, Pageable pageable);


    @Query("select s from Store s where (:distName is null or s.dist.distName LIKE %:distName%)"+
    "and (:branchName is null or s.branch.branchName LIKE %:branchName%)"+
    "and (:storeName is null or s.storeName LIKE %:storeName%)"+
    "and (:storeGrade is null or s.storeGrade = %:storeGrade%)"+
    "and (:storeCd is null or s.storeCd LIKE %:storeCd%)"+
    "and (:storeChiefEmail is null or s.storeChiefEmail LIKE %:storeChiefEmail%)"+
    "and (:storeChief is null or s.storeChief LIKE %:storeChief%)"+
    "and (:brandName is null or s.brand.brandName LIKE %:brandName%)"+
    "and (:storeStatus is null or s.storeStatus = %:storeStatus%)"+
    "and (:storePType is null or s.storePType = %:storePType%)"
    )

    Page<Store> multiSearch(@Param("distName") String distName,
                            @Param("branchName") String branchName,
                            @Param("storeName") String storeName,
                            @Param("storeGrade") StoreGrade storeGrade,
                            @Param("storeCd") String storeCd,
                            @Param("storeChiefEmail") String storeChiefEmail,
                            @Param("storeChief") String storeChief,
                            @Param("brandName") String brandName,
                            @Param("storeStatus") StoreStatus storeStatus,
                            @Param("storePType") StorePType storePType,
                            Pageable pageable);







//    "and (:name is null or s.dist.distEmail LIKE %:name% or s.branch.branchEmail LIKE %:name% or s.member.memberemail LIKE %:name%)"

//    @RequestParam(value="distName", required = false) String distName,
//    @RequestParam(value="branchName", required = false) String branchName,
//    @RequestParam(value="storeName", required = false) String storeName,
//    @RequestParam(value="storeGrade", required = false) String storeGrade,
//    @RequestParam(value="storeCd", required = false) String storeCd,
//    @RequestParam(value="storeChiefEmail", required = false) String storeChiefEmail,
//    @RequestParam(value="storeChief", required = false) String storeChief,
//    @RequestParam(value="brandName", required = false) String brandName,
//    @RequestParam(value="storeStatus", required = false) String storeStatus,
//    @RequestParam(value="storePType", required = false) String storePType




}
