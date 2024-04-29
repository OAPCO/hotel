package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.MenuCate;
import jakarta.persistence.Column;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCateDTO {


    ModelMapper modelMapper = new ModelMapper();

    private Long menuCateIdx;

    private String menuCateName; //메뉴 카테고리 이름

    private String tblStoreStoreIdx;//매장 식별키

    private LocalDateTime regdate;

    private LocalDateTime moddate;

    private List<DetailmenuDTO> detailMenuDTOList;



}
