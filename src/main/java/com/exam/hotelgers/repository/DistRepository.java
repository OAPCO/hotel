package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.DistChief;
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
public interface DistRepository extends JpaRepository<Dist, Long> {
    //사용자 아이디로 조회

    @Query("select d from Dist d where d.distCd LIKE %:#{#searchDTO.distCd}%")
    Optional<Dist> distCheckGet(SearchDTO searchDTO);

    Optional<Dist> findByDistCd(String distCd);
    Optional<Dist> findByDistName(String distName);

    Optional<Dist> findByDistIdx(Long distIdx);

    List<Dist> findByDistChief_DistChiefId(String distChiefId);
    Optional<Dist> findByManagerList_ManagerId(String managerId);


    @Query("SELECT d.distCd FROM Dist d")
    List<String> findAllDistCds();



    @Query("select d from Dist d where (:#{#searchDTO.distName} is null or d.distName LIKE %:#{#searchDTO.distName}%)"+
            "and (:#{#searchDTO.distChiefName} is null or d.distChief.distChiefName LIKE %:#{#searchDTO.distChiefName}%)")
    Page<Dist> multiSearchadminsdm(SearchDTO searchDTO, Pageable pageable);


    @Query("select d from DistChief d where (:#{#searchDTO.distChiefName} is null or d.distChiefName LIKE %:#{#searchDTO.distChiefName}%)")
    Page<DistChief> multiSearchadmdr(SearchDTO searchDTO, Pageable pageable);


    @Query("select d from Dist d where (:#{#searchDTO.distName} is null or d.distName LIKE %:#{#searchDTO.distName}%)"+
            "and (:#{#searchDTO.distChiefEmail} is null or d.distChief.distChiefEmail LIKE %:#{#searchDTO.distChiefEmail}%)"+
            "and (:#{#searchDTO.distChiefName} is null or d.distChief.distChiefName LIKE %:#{#searchDTO.distChiefName}%)"+
            "and (:#{#searchDTO.distTel} is null or d.distTel LIKE %:#{#searchDTO.distTel}%)")
    Page<Dist> multiSearchmemadmin(SearchDTO searchDTO,
                                   Pageable pageable);



}
