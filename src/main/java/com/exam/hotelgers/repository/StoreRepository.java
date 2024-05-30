package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store>findByStoreIdx(Long storeIdx);

    Optional<Store> findByStoreCd(String storeCd);

    List<Store> findByBrandCd(String brandCd);

    //storeIdx로 매장명 찾기
    @Query("select s.storeName from Store s where s.storeIdx = :storeIdx")
    String findStoreName(Long storeIdx);

    //roomOrderidx로 매장명 찾기
    @Query("select s.storeName from Store s join RoomOrder o on s.storeIdx = o.storeIdx where o.roomorderIdx = :roomorderIdx")
    String findStoreRoomOrderIdx(Long roomorderIdx);

    

    //로그인중인 매장주 아이디로 매장 조회
    @Query("select s from Store s where (s.managerId LIKE %:managerId%)")
    Optional<Store> managerToStoreSearch(String managerId);


    //로그인중인 총판장 아이디가 맞는 매장과 + 매장에 해당하는 브랜드,매니저 조회
    @Query("SELECT s, b, m FROM Store s LEFT JOIN Brand b ON s.brandCd = b.brandCd " +
            "LEFT JOIN Manager m ON s.managerId = m.managerId " +
            "where (s.dist.distChief.distChiefId LIKE %:userId%)")
    Page<Object[]> storeToBrand(Pageable pageable, String userId);


    //store name을 입력받았을 때 가입시 필요한 유니크 컬럼인 storeCd 값을 구하기 위한 쿼리
    @Query("select s from Store s where (:storeName is null or s.storeName LIKE %:storeName%)")
    Optional<Store> storeNameSearch(String storeName);


    //매장생성시 해당 매니저가 이미 매장을 가지고 있는지 확인
    Optional<Store> findByManagerId(String ManagerId);


    //어드민의 매장 조회용
    Page<Store> findAll(Pageable pageable);


    //어드민의 매장 다중검색
    @Query("select s,b,m from Store s left join Brand b on s.brandCd = b.brandCd left join Manager m on s.managerId = m.managerId " +
            "where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)" +
            "and (:#{#searchDTO.storeName} is null or s.storeName LIKE %:#{#searchDTO.storeName}%)" +
            "and (:#{#searchDTO.storeCd} is null or s.storeCd LIKE %:#{#searchDTO.storeCd}%)" +
            "and (:#{#searchDTO.managerName} is null or m.managerName LIKE %:#{#searchDTO.managerName}%)" +
            "and (:#{#searchDTO.brandName} is null or b.brandName LIKE %:#{#searchDTO.brandName}%)" +
            "and (:#{#searchDTO.storeStatus} is null or s.storeStatus = %:#{#searchDTO.storeStatus}%)" +
            "and (:#{#searchDTO.storePType} is null or s.storePType = %:#{#searchDTO.storePType}%)" +
            "and (:#{#searchDTO.storeGrade} is null or s.storeGrade = %:#{#searchDTO.storeGrade}%)")
    Page<Object[]> adminStoreSearch(SearchDTO searchDTO,
                                    Pageable pageable);


    //매장조회 다중검색
    @Query("select s,b,m from Store s left join Brand b on s.brandCd = b.brandCd left join Manager m on s.managerId = m.managerId " +
            "where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)" +
            "and (:#{#searchDTO.storeName} is null or s.storeName LIKE %:#{#searchDTO.storeName}%)" +
            "and (:#{#searchDTO.storeCd} is null or s.storeCd LIKE %:#{#searchDTO.storeCd}%)" +
            "and (:#{#searchDTO.managerName} is null or m.managerName LIKE %:#{#searchDTO.managerName}%)" +
            "and (:#{#searchDTO.brandName} is null or b.brandName LIKE %:#{#searchDTO.brandName}%)" +
            "and (:#{#searchDTO.storeStatus} is null or s.storeStatus = %:#{#searchDTO.storeStatus}%)" +
            "and (:#{#searchDTO.storePType} is null or s.storePType = %:#{#searchDTO.storePType}%)" +
            "and (:#{#searchDTO.storeGrade} is null or s.storeGrade = %:#{#searchDTO.storeGrade}%)" +
            "and (s.dist.distChief.distChiefId LIKE %:userId%)"
    )
    Page<Object[]> multiSearch(SearchDTO searchDTO,
                               Pageable pageable, String userId);


    //dist와 brand 선택시 매장 조회(셀렉트박스에 사용중)
    @Query("select s from Store s where (:#{#searchDTO.distName} is null or s.dist.distName LIKE %:#{#searchDTO.distName}%)" +
            "and (:#{#searchDTO.brandCd} is null or s.brandCd LIKE %:#{#searchDTO.brandCd}%)"
    )
    List<Store> distbrandOfStore(SearchDTO searchDTO);

    //read할때 StoreIdx를 이용해서 상위 객체들 한번에 가져오기
    @Query("SELECT s, b, d, dc, m FROM Store s " +
            "JOIN Dist d ON d.distCd = s.dist.distCd " +
            "JOIN Brand b ON b.brandCd = s.brandCd " +
            "JOIN DistChief dc ON dc.distChiefIdx = d.distChief.distChiefIdx " +
            "JOIN Manager m ON m.dist.distCd = d.distCd " +
            "WHERE s.storeIdx = :storeIdx")
    List<Object[]> storeBrandDistDistChiefManager(Long storeIdx);


    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.regionCd LIKE %:keyword% OR s.storeAddr LIKE %:keyword%")
    List<Store> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT s FROM Store s JOIN FETCH s.menuCateList m JOIN FETCH m.detailMenuList WHERE s.storeIdx = ?1")
    Optional<Store> findByStoreIdxWithDetails(Long storeIdx);
    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.menuCateList WHERE s.storeIdx = :storeIdx")
    Optional<Store> findWithMenuCateByStoreIdx(@Param("storeIdx") Long storeIdx);

    @Query("SELECT m FROM MenuCate m LEFT JOIN FETCH m.detailMenuList WHERE m in :menuList")
    List<MenuCate> findWithDetailMenuByMenuCateList(@Param("menuList") List<MenuCate> menuList);

    @Query(nativeQuery = true, value = "SELECT * FROM Store ORDER BY RAND() LIMIT 5")
    List<Store> findRandomStores();
    
    
    //총판의 매장 수 구하기
    @Query("SELECT count(s) FROM Store s WHERE s.dist.distIdx = :distIdx")
    int findDistOfStoreCount(Long distIdx);


    //로그인 한 매니저의 매장 찾기
    @Query("select s from Store s where (s.managerId LIKE %:userid%)")
    Optional<Store> searchStoreuserId (String userid);



    //로그인 한 총판장이 소유한 매장 목록
    @Query("select d.storeList from Dist d where d.distChief.distChiefId = :userid")
    List<Store> searchStoreDistChiefId (String userid);



    //회원 본인이 "묵고있는" 호텔을 반환받음
    @Query("select s from Store s join RoomOrder r on r.storeIdx = s.storeIdx where r.roomorderIdx = :roomorderIdx " +
            "and r.roomStatus = 2")
    Optional<Store> findCheckinStore(Long roomorderIdx);



    //매장 정보 수정 쿼리들
    @Modifying
    @Query("UPDATE Store s " +
            "SET s.storeSummary = :storeSummary " +
            "WHERE s.storeIdx = :storeIdx")
    void storeSummaryModify(String storeSummary,Long storeIdx);

    @Modifying
    @Query("UPDATE Store s " +
            "SET s.cancelCharge = :cancelCharge " +
            "WHERE s.storeIdx = :storeIdx")
    void cancelChargeModify(double cancelCharge,Long storeIdx);


    @Modifying
    @Query("UPDATE Store s " +
            "SET s.storeMessage = :storeMessage " +
            "WHERE s.storeIdx = :storeIdx")
    void storeMessageModify(String storeMessage,Long storeIdx);

    @Modifying
    @Query("UPDATE Store s " +
            "SET s.storeCheckinTime = :storeCheckinTime " +
            "WHERE s.storeIdx = :storeIdx")
    void storeCheckinTimeModify(String storeCheckinTime,Long storeIdx);

    @Modifying
    @Query("UPDATE Store s " +
            "SET s.storeCheckoutTime = :storeCheckoutTime " +
            "WHERE s.storeIdx = :storeIdx")
    void storeCheckoutTimeModify(String storeCheckoutTime,Long storeIdx);



    //호텔 이미지 수정
    @Modifying
    @Query("UPDATE Store s " +
            "SET s.storeimgName = :storeimgName " +
            "WHERE s.storeIdx = :storeIdx")
    void storeImgModify(String storeimgName ,Long storeIdx);



    //listboard 줄 추가
    @Modifying
    @Query("UPDATE Store s " +
            "SET s.roomCount = s.roomCount + 12 " +
            "WHERE s.storeIdx = :storeIdx")
    void roomCardAdd(Long storeIdx);

    //매장의 수수료 검색
    @Query("select s.cancelCharge from Store s where s.storeIdx = :storeIdx")
    double searchStoreCharge(Long storeIdx);

}