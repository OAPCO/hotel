package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.entity.Order;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, Search {



    //사용자 아이디로 조회
    Optional<Order> findByOrderCd(String orderCd);

    Optional<Order> findByOrderIdx(Long orderIdx);


//    @Query("SELECT s FROM Store s WHERE s.dist.distName like %:distName%")
//    Page<Store> distNameSearch(String distName, Pageable pageable);


    @Query("select s from Store s where (:distName is null or s.dist.distName LIKE %:distName%)"+
    "and (:branchName is null or s.branch.branchName LIKE %:branchName%)"+
    "and (:storeName is null or s.storeName LIKE %:storeName%)"+ "and (:storePType is null or s.storePType = %:storePType%)"+
    "and (:storeStatus is null or s.storeStatus = %:storeStatus%)"
    )
    Page<Order> multiSearch(@Param("distName") String distName,
                            @Param("branchName") String branchName,
                            @Param("storeName") String storeName,
                            @Param("storePType") StorePType storePType,
                            @Param("storeStatus") StoreStatus storeStatus,
                            Pageable pageable);






}
