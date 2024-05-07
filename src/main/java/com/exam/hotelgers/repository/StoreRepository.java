package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreCd(String storeCd);

    Optional<Store> findByStoreIdx(Long storeIdx);


    //로그인중인 매장주 아이디로 매장 조회
    @Query("select s from Store s where (s.managerId LIKE %:managerId%)")
    Optional<Store> managerToStoreSearch(String managerId);


    //로그인중인 총판장 아이디로 매장 조회
//    @Query("select s from Store s where (s.dist.distChief.distChiefId LIKE %:distChiefId%)")
//    Page<Store> distChiefToStoreSearch(String distChiefId);


//    @Query("select b from Brand b join Store s where b.brandCd LIKE s.brandCd")
//    Optional<Brand> storeToBrand();

    @Query("SELECT s, b, m FROM Store s LEFT JOIN Brand b ON s.brandCd = b.brandCd " +
            "LEFT JOIN Manager m ON s.managerId = m.managerId " +
            "where (s.dist.distChief.distChiefId LIKE %:userId%)")
    Page<Object[]> storeToBrand(Pageable pageable,String userId);







    //store name을 입력받았을 때 가입시 필요한 유니크 컬럼인 storeCd 값을 구하기 위한 쿼리
    @Query("select s from Store s where (:storeName is null or s.storeName LIKE %:storeName%)")
    Optional<Store> storeNameSearch(String storeName);




    @Query("select s,b,m from Store s left join Brand b on s.brandCd = b.brandCd left join Manager m on s.managerId = m.managerId " +
            "where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
    "and (:#{#searchDTO.storeName} is null or s.storeName LIKE %:#{#searchDTO.storeName}%)"+
    "and (:#{#searchDTO.storeCd} is null or s.storeCd LIKE %:#{#searchDTO.storeCd}%)"+
    "and (:#{#searchDTO.managerName} is null or s.managerId LIKE %:#{#searchDTO.managerName}%)"+
    "and (:#{#searchDTO.brandName} is null or s.brandCd LIKE %:#{#searchDTO.brandName}%)"+
    "and (:#{#searchDTO.storeStatus} is null or s.storeStatus = %:#{#searchDTO.storeStatus}%)"+
    "and (:#{#searchDTO.storePType} is null or s.storePType = %:#{#searchDTO.storePType}%)"+
            "and (:#{#searchDTO.storeGrade} is null or s.storeGrade = %:#{#searchDTO.storeGrade}%)"+
            "and (s.dist.distChief.distChiefId LIKE %:userId%)"
    )
    Page<Object[]> multiSearch(SearchDTO searchDTO,
                            Pageable pageable, String userId);



    //수정전
//    @Query("select s from Store s where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
//            "and (:#{#searchDTO.brandName} is null or s.brand.brandName LIKE %:#{#searchDTO.brandName}%)"
//    )
    @Query("select s from Store s where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
            "and (:#{#searchDTO.brandName} is null or s.brandCd LIKE %:#{#searchDTO.brandName}%)"
    )
    List<Store> distbrandOfStore(SearchDTO searchDTO);



}
