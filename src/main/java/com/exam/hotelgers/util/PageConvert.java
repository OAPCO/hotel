package com.exam.hotelgers.util;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class PageConvert {

    public static Map<String, Integer> Pagination(Page<?> page) {
        int currentPage = page.getNumber()+1;
        int totalPages = page.getTotalPages();
        int blockLimit = 10;

        Map<String, Integer> pageinfo = new HashMap<>();

        int s = Math.max(1, currentPage-blockLimit/2);
        int e = Math.min(s+blockLimit-1, totalPages);
        int p = Math.max(1, currentPage-1);
        int n = Math.min(currentPage+1, totalPages);
        int l =totalPages;


        pageinfo.put("startPage", s);
        pageinfo.put("endPage", e);
        pageinfo.put("prevPage", p);
        pageinfo.put("currentPage", currentPage);
        pageinfo.put("nextPage", n);
        pageinfo.put("lastPage", l);

        return pageinfo;
    }
}