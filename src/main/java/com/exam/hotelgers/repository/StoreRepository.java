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


    //로그인중인 매장주 아이디로 매장 조회
    @Query("select s from Store s where (s.managerId LIKE %:managerId%)")
    Optional<Store> managerToStoreSearch(String managerId);


    //로그인중인 총판장 아이디가 맞는 매장과 + 매장에 해당하는 브랜드,매니저 조회
    @Query("SELECT s, b, m FROM Store s LEFT JOIN Brand b ON s.brandCd = b.brandCd " +
            "LEFT JOIN Manager m ON s.managerId = m.managerId " +
            "where (s.dist.distChief.distChiefId LIKE %:userId%)")
    Page<Object[]> storeToBrand(Pageable pageable,String userId);



    //store name을 입력받았을 때 가입시 필요한 유니크 컬럼인 storeCd 값을 구하기 위한 쿼리
    @Query("select s from Store s where (:storeName is null or s.storeName LIKE %:storeName%)")
    Optional<Store> storeNameSearch(String storeName);


    //매장생성시 해당 매니저가 이미 매장을 가지고 있는지 확인
    Optional<Store> findByManagerId(String ManagerId);



    //매장조회 다중검색
    @Query("select s,b,m from Store s left join Brand b on s.brandCd = b.brandCd left join Manager m on s.managerId = m.managerId " +
            "where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
    "and (:#{#searchDTO.storeName} is null or s.storeName LIKE %:#{#searchDTO.storeName}%)"+
    "and (:#{#searchDTO.storeCd} is null or s.storeCd LIKE %:#{#searchDTO.storeCd}%)"+
    "and (:#{#searchDTO.managerName} is null or m.managerName LIKE %:#{#searchDTO.managerName}%)"+
    "and (:#{#searchDTO.brandName} is null or b.brandName LIKE %:#{#searchDTO.brandName}%)"+
    "and (:#{#searchDTO.storeStatus} is null or s.storeStatus = %:#{#searchDTO.storeStatus}%)"+
    "and (:#{#searchDTO.storePType} is null or s.storePType = %:#{#searchDTO.storePType}%)"+
            "and (:#{#searchDTO.storeGrade} is null or s.storeGrade = %:#{#searchDTO.storeGrade}%)"+
            "and (s.dist.distChief.distChiefId LIKE %:userId%)"
    )
    Page<Object[]> multiSearch(SearchDTO searchDTO,
                            Pageable pageable, String userId);



    //dist와 brand 선택시 매장 조회(셀렉트박스에 사용중)
    @Query("select s from Store s where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)"+
            "and (:#{#searchDTO.brandName} is null or s.brandCd LIKE %:#{#searchDTO.brandName}%)"
    )
    List<Store> distbrandOfStore(SearchDTO searchDTO);



}
