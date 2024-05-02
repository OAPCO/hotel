package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {


    Optional<Admin> findByAdminId(String adminid);

    Optional<Admin> findByAdminIdx(Long adminIdx);

//    @Query("select d from Dist d join Store s,DistChief c,Manager m where (:distName is null or d.distName LIKE %:distName%)"+
//            "and (:distChiefEmail is null or d.distChief.distChiefEmail LIKE %:distChiefEmail%)"+
//            "and (:distChief is null or d.distChief.distChiefName LIKE %:distChief%)"+
//            "and (:distTel is null or d.distTel LIKE %:distTel%)"
//
//    )
//    Page<Dist> multiSearchmemadmin2(@Param("distName") String distName,
//                                    @Param("distChiefEmail") String distChiefEmail,
//                                    @Param("distChief") String distChief,
//                                    @Param("distTel") String distTel,
//                                    Pageable pageable);


    @Query("select a.adminId from Admin a where (a.adminId LIKE %:userid%)")
    List<String> registerCheck (String userid);


}
