package com.exam.hotelgers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageRequestDTO {

    //컨트롤러에서 값을 받을 때 각종 정보를 전부 받는 객체

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private Integer size = 10;

    private String keyword; //검색어

    private String link;

    public Pageable getPageable(String... props){

        Pageable pageable = PageRequest.of(this.page-1,this.size,Sort.by(props).descending());

        return pageable;
    }

    public String getLink(){
        if(link == null){
            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);
            if(keyword != null){
                try{
                    builder.append("&keyword="+ URLEncoder.encode(keyword,"utf-8"));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }

            link = builder.toString(); //page=3&type=t&keyword=신짱구
        }
        return link;
    }
}
