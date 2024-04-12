package com.exam.hotelgers.repository.search;

import com.exam.hotelgers.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

public interface Search {


    Page<Store> selectSearch(String distName,String branchName, String brandName, Pageable pageable);
}



//@RequestParam(value="branchName", required = false) String branchName,
//@RequestParam(value="storeName", required = false) String storeName,
//@RequestParam(value="storeGrade", required = false) String storeGrade,
//@RequestParam(value="storeCd", required = false) String storeCd,
//@RequestParam(value="storeChiefEmail", required = false) String storeChiefEmail,
//@RequestParam(value="storeChief", required = false) String storeChief,
//@RequestParam(value="brandName", required = false) String brandName,
//@RequestParam(value="storeStatus", required = false) String storeStatus,
//@RequestParam(value="storePType", required = false) String storePType