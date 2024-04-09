package com.exam.hotelgers.dto;
//시스템 전체에서 사용할 전역변수
public class AppData {
    //매장 일련변호를 저장할 전역변수
    private static Long storeId;

    public static Long getStoreId() {
        return storeId;
    }

    public static void setStoreId(Long id) {
        storeId = id;
    }
}