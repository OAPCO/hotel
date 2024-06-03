package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

    @Query("select a.adminId from Admin a where (a.adminId LIKE %:userid%)")
    List<String> registerCheck (String userid);

    Optional<Admin> findByAdminId(String adminid);

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


//    @Query("SELECT s, b, m FROM Store s LEFT JOIN Brand b ON s.brandCd = b.brandCd " +
//            "LEFT JOIN Manager m ON s.managerId = m.managerId " +
//            "where (s.dist.distChief.distChiefId LIKE %:userId%)")
//    Page<Object[]> storeToBrand123(Pageable pageable,String userId);


//    @Query("SELECT d, m, n FROM DistChief d, Manager m, Member n ")
////    +"where (d.distChiefName LIKE %:userId%)")
//    Page<Object[]> memberListSearch(Pageable pageable, SearchDTO searchDTO);




    @Query("SELECT d FROM DistChief d")
    List<DistChief> distChiefListSearch();

    @Query("SELECT m FROM Manager m")
    List<Manager> managerListSearch();

    @Query("SELECT n FROM Member n")
    List<Member> memberListSearch();



    @Query("SELECT d FROM DistChief d " +
            "where (:#{#searchDTO.name} is null or d.distChiefName LIKE %:#{#searchDTO.name}%)"+
            "and (:#{#searchDTO.id} is null or d.distChiefId LIKE %:#{#searchDTO.id}%)"+
            "and (:#{#searchDTO.roleType} is null or d.roleType = :#{#searchDTO.roleType})")
    List<DistChief> distChiefListSearch1(SearchDTO searchDTO);


    @Query("SELECT m FROM Manager m " +
            "where (:#{#searchDTO.name} is null or m.managerName LIKE %:#{#searchDTO.name}%)"+
            "and (:#{#searchDTO.id} is null or m.managerId LIKE %:#{#searchDTO.id}%)"+
            "and (:#{#searchDTO.roleType} is null or m.roleType = %:#{#searchDTO.roleType}%)")
    List<Manager> managerListSearch1(SearchDTO searchDTO);


    @Query("SELECT m FROM Member m " +
            "where (:#{#searchDTO.name} is null or m.memberName LIKE %:#{#searchDTO.name}%)"+
            "and (:#{#searchDTO.memberId} is null or m.memberId LIKE %:#{#searchDTO.memberId}%)"+
            "and (:#{#searchDTO.roleType} is null or m.roleType = %:#{#searchDTO.roleType}%)")
    List<Member> memberListSearch1(SearchDTO searchDTO);







    //회원 정보 수정

}
