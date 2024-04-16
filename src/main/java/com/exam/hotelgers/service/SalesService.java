package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.SalesDTO;
import com.exam.hotelgers.dto.SalesDTO;
import com.exam.hotelgers.entity.Sales;
import com.exam.hotelgers.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalesService {
    private final SalesRepository salesRepository;
    private final ModelMapper modelMapper;

    //삽입
    public Sales insert(SalesDTO salesDTO) {
        Sales storeMember = modelMapper.map(salesDTO, Sales.class);
        Sales result = salesRepository.save(storeMember);

        return result;
    }

    //수정
    public SalesDTO update(SalesDTO salesDTO) {
        Optional<Sales> search = salesRepository.findById(salesDTO.getSalesidx());

        if(search.isPresent()) {
            Sales storeMember = modelMapper.map(salesDTO, Sales.class);
            salesRepository.save(storeMember);
        }
        SalesDTO result = search.map(data ->modelMapper.map(data, SalesDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long id) {
        salesRepository.deleteById(id);
    }

    //전체조회
    public Page<SalesDTO> select(Pageable page, SalesDTO salesDTO) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                            Sort.by(Sort.Direction.DESC,"salesidx"));

        Page<Sales> salesEntities = salesRepository.search(
                salesDTO.getDistributor_organization(),
                salesDTO.getBranch(),
                salesDTO.getStorename(),
                salesDTO.getDate(),
                salesDTO.getPayment_method(),
                salesDTO.getStore(),
                salesDTO.getProcessing_status(),
                pageable);


        Page<SalesDTO> result = salesEntities.map(data->modelMapper.map(data,SalesDTO.class));

        return result;
    }
    public Page<SalesDTO> salesList(Pageable pageable, String type, LocalDate sdate) {
        LocalDateTime now = LocalDateTime.now(); //현재날짜
        LocalDateTime oneMonthAgo = now.minusMonths(1); //1개월
        LocalDateTime threeMonthAgo = now.minusMonths(3); //3개월
        LocalDateTime sixMonthsAgo = now.minusMonths(6); //6개월
        LocalDateTime oneYearsAgo = now.minusYears(1); //1년

        //화면에      페이지번호 1, 2, 3, 4, 5, 6
        //데이터베이스 페이지번호 0, 1, 2, 3, 4, 5
        //화면에 페이지번호를 데이터베이스 페이지 번호로 계산
        int currentPage = pageable.getPageNumber()-1;
        //한 페이지에 읽어올 개수(지정한 페이지에서 읽어올 개수)
        int guestLimits = 10;
        //현재지정한 페이지를 지정한 개수만큼 읽어오는데 id를 기준으로 내림차순
        Pageable salespage = PageRequest.of(currentPage, guestLimits,
                Sort.by(Sort.Direction.DESC,"salesidx"));

        Page<Sales> sales;

        //if문을 이용해서 각 조건에 따른 조회처리
        if(type != null && !type.trim().isEmpty()) {
            System.out.println(type);
            sales = salesRepository.findByType(oneMonthAgo,threeMonthAgo,
                    sixMonthsAgo,oneYearsAgo, salespage);
        } else if(sdate != null) {
            LocalDateTime startDateTime = sdate.atStartOfDay();
            LocalDateTime endDateTime = startDateTime.plusDays(1);

            sales =  salesRepository.findByModdateBetween(startDateTime,
                    endDateTime, salespage);
        } else {
            sales = salesRepository.findAll(salespage);
        }

        //데이터값 변환(ModelMapper DTO<->Entity), Page에 대한 변환X

        Page<SalesDTO> salesDTOS = sales.map(data->modelMapper.map(data,
                SalesDTO.class));
        /*Page<PageDTO> guestDTOS = guestEntities.map(
                data->PageDTO.builder()
                        .id(data.getId()).guest(data.getPage())
                        .detail(data.getDetail()).price(data.getPrice())
                        .quantity(data.getQuantity())
                        .build()); */
        return salesDTOS;
    }

    //개별조회
    public SalesDTO read(Long id) {
        Optional<Sales> storeMember = salesRepository.findById(id);
            //SalesDTO result = modelMapper.map(storeMember, SalesDTO.class);
            SalesDTO result = storeMember.map(data->modelMapper.map(data, SalesDTO.class)).orElse(null);

            return result;
    }
}

