package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Brand;
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
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("select b from Brand b where b.brandCd LIKE %:#{#searchDTO.brandCd}%")
    Optional<Brand> brandCheckGet(SearchDTO searchDTO);

    Optional<Brand> findByBrandIdx(Long brandIdx);


    @Query("select b,d from Brand b left join Dist d on b.distCd = d.distCd where (d.distChief.distChiefId LIKE %:userId%)")
    Page<Object[]> distChiefToBrandSearch(Pageable pageable,String userId);



    @Query("select b from Brand b where (:#{#searchDTO.distName} is null or b.distCd LIKE %:#{#searchDTO.distName}%)")
    List<Brand> distOfBrand(SearchDTO searchDTO);

    @Query("SELECT b, m, d, dc FROM Brand b " +
            "JOIN Dist d ON d.distCd = b.distCd " +
            "JOIN Manager m ON m.dist.distCd = d.distCd " +
            "JOIN DistChief dc ON dc.distChiefIdx = d.distChief.distChiefIdx " +
            "WHERE b.brandIdx = :brandIdx")
    List<Object[]> brandManagerDistDistChief(Long brandIdx);

}
