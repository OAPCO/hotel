package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.StoreMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMemberRepository extends JpaRepository <StoreMember, Long> {

    @Query("SELECT u FROM StoreMember u WHERE (:storeMemberEmail IS NULL OR u.storeMemberEmail LIKE %:storeMemberEmail%) " +
            "AND (:storeMemberName IS NULL OR u.storeMemberName LIKE %:storeMemberName%) " +
            "AND (:storeMemberTel IS NULL OR u.storeMemberTel LIKE %:storeMemberTel%) " +
            "AND (:storeMemberState IS NULL OR u.storeMemberState = :storeMemberState) "+
            "AND (:storeMemberAuth IS NULL OR u.storeMemberAuth LIKE %:storeMemberAuth%) "+
            "AND (:storeDistributorIdx IS NULL OR u.storeDistributorIdx = :storeDistributorIdx) "+
            "AND (:storeBranchIdx IS NULL OR u.storeBranchIdx = :storeBranchIdx) "+
            "AND (:storeIdx IS NULL OR u.storeIdx = :storeIdx) ")
    Page<StoreMember> search(@Param("storeMemberEmail") String storeMemberEmail, //아이디
                              @Param("storeMemberName") String storeMemberName, //이름
                              @Param("storeMemberTel") String storeMemberTel, //전화
                              @Param("storeMemberState") Integer storeMemberState, //상태
                              @Param("storeMemberAuth") String storeMemberAuth, //권한
                              @Param("storeDistributorIdx") Integer storeDistributorIdx, //총판
                             @Param("storeBranchIdx") Integer storeBranchIdx, //지사
                             @Param("storeIdx") Integer storeIdx,
                             Pageable pageable); //매장
}
