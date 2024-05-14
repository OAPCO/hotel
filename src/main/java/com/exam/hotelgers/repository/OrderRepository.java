package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
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


    Optional<Order> findByOrderCd(Long orderIdx);

    Optional<Order> findByOrderIdx(Long orderIdx);



//    @Query("select o from Order o where" +
//            "(:#{#searchDTO.distName} is null or o.dist.distName LIKE %:#{#searchDTO.distName}%)"+
//            "and (:#{#searchDTO.storeName} is null or o.store.storeName LIKE %:#{#searchDTO.storeName}%)"+
//            "and (:#{#searchDTO.storePType} is null or o.store.storePType = :#{#searchDTO.storePType})"+
//            "and (:#{#searchDTO.storeStatus} is null or o.store.storeStatus = :#{#searchDTO.storeStatus})"
//    )
//    Page<Order> multiSearch(SearchDTO searchDTO,
//                             Pageable pageable);
//
//
//
//
//
//
//
//    @Query("select o from Order o where (:distName is null or o.dist.distName LIKE %:distName%)"+
//            "and (:storeName is null or o.store.storeName LIKE %:storeName%)"+
//            "and (:orderCd is null or o.orderCd LIKE %:orderCd%)"+
//            "and (:roomCd is null or o.room.roomCd LIKE %:roomCd%)"
//    )
//    Page<Order> orderListSearch(@Param("distName") String distName,
//                            @Param("storeName") String storeName,
//                            @Param("orderCd") String orderCd,
//                            @Param("roomCd") String roomCd,
//                            Pageable pageable);
}
