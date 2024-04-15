package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    Optional<Order> findByOrderCd(String orderCd);

    Optional<Order> findByOrderIdx(Long orderIdx);


    @Query("select o from Order o where (:distName is null or o.dist.distName LIKE %:distName%)"+
            "and (:branchName is null or o.branch.branchName LIKE %:branchName%)"+
            "and (:storeName is null or o.store.storeName LIKE %:storeName%)"+
            "and (:storePType is null or o.store.storePType = :storePType)"+
            "and (:storeStatus is null or o.store.storeStatus = :storeStatus)"
    )
    Page<Order> multiSearch(@Param("distName") String distName,
                            @Param("branchName") String branchName,
                            @Param("storeName") String storeName,
                            @Param("storePType") StorePType storePType,
                            @Param("storeStatus") StoreStatus storeStatus,
                            Pageable pageable);
}
