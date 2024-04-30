package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
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

    Optional<Dist> findByDistCd(String distCd);
    Optional<Dist> findByDistName(String distName);

    Optional<Dist> findByDistIdx(Long distIdx);

    List<Dist> findByDistChief_DistChiefId(String distChiefId);


    @Query("SELECT d.distCd FROM Dist d")
    List<String> findAllDistCds();




    @Query("select d from Dist d where (:distName is null or d.distName LIKE %:distName%)"+
            "and (:distChief is null or d.distChief.distChiefName LIKE %:distChief%)"
    )
    Page<Dist> multiSearchadminsdm(@Param("distName") String distName,
                                   @Param("distChief") String distChief,
                                   Pageable pageable);

    @Query("select d from Dist d where (:distChief is null or d.distChief.distChiefName LIKE %:distChief%)"
    )
    Page<Dist> multiSearchadmdr(@Param("distChief") String distChief,
                                Pageable pageable);


    @Query("select d from Dist d where (:distName is null or d.distName LIKE %:distName%)"+
            "and (:distChiefEmail is null or d.distChief.distChiefEmail LIKE %:distChiefEmail%)"+
            "and (:distChief is null or d.distChief.distChiefName LIKE %:distChief%)"+
            "and (:distTel is null or d.distTel LIKE %:distTel%)"

    )

    Page<Dist> multiSearchmemadmin(@Param("distName") String distName,
                                   @Param("distChiefEmail") String distChiefEmail,
                                   @Param("distChief") String distChief,
                                   @Param("distTel") String distTel,
                                   Pageable pageable);



}
