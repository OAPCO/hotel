package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.SalesMonthDTO;
import com.exam.hotelgers.entity.SalesMonth;
import com.exam.hotelgers.repository.SalesMonthRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalesMonthService {
    private final SalesMonthRepository salesMonthRepository;
    private final ModelMapper modelMapper;

    //삽입
    public SalesMonth insert(SalesMonthDTO salesMonthDTO) {
        SalesMonth salesMonth = modelMapper.map(salesMonthDTO, SalesMonth.class);
        SalesMonth result = salesMonthRepository.save(salesMonth);

        return result;
    }

    //수정
    public SalesMonthDTO update(SalesMonthDTO salesMonthDTO) {
        Optional<SalesMonth> search = salesMonthRepository.findById(salesMonthDTO.getSalesmonthidx());

        if(search.isPresent()) {
            SalesMonth storeMember = modelMapper.map(salesMonthDTO, SalesMonth.class);
            salesMonthRepository.save(storeMember);
        }
        SalesMonthDTO result = search.map(data ->modelMapper.map(data, SalesMonthDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long id) {
        salesMonthRepository.deleteById(id);
    }

    //전체조회
    public Page<SalesMonthDTO> select(Pageable page, SalesMonthDTO salesMonthDTO) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                            Sort.by(Sort.Direction.DESC,"salesmonthidx"));

        /*Page<SalesMonth> salesEntities = salesMonthRepository.search(
                salesMonthDTO.getDistributor_organization(),
                salesMonthDTO.getBranch(),
                salesMonthDTO.getStorename(),
                salesMonthDTO.getDate(),
                salesMonthDTO.getPayment_method(),
                salesMonthDTO.getStore(),
                salesMonthDTO.getProcessing_status(),
                pageable);*/
        Page<SalesMonth> salesMonths = salesMonthRepository.search(
                salesMonthDTO.getDistributor_organization(),//총판조직
                salesMonthDTO.getBranch(),//지사
                salesMonthDTO.getStore(),//매장
                salesMonthDTO.getStartDate(),//시작일
                salesMonthDTO.getEndDate(),//종료일
                pageable);


        Page<SalesMonthDTO> result = salesMonths.map(data->modelMapper.map(data,SalesMonthDTO.class));

        return result;
    }
//    public Page<SalesMonthDTO> salesList(Pageable pageable, String type, LocalDate sdate) {
//        LocalDateTime now = LocalDateTime.now(); //현재날짜
//        LocalDateTime oneMonthAgo = now.minusMonths(1); //1개월
//        LocalDateTime threeMonthAgo = now.minusMonths(3); //3개월
//        LocalDateTime sixMonthsAgo = now.minusMonths(6); //6개월
//        LocalDateTime oneYearsAgo = now.minusYears(1); //1년
//
//        //화면에      페이지번호 1, 2, 3, 4, 5, 6
//        //데이터베이스 페이지번호 0, 1, 2, 3, 4, 5
//        //화면에 페이지번호를 데이터베이스 페이지 번호로 계산
//        int currentPage = pageable.getPageNumber()-1;
//        //한 페이지에 읽어올 개수(지정한 페이지에서 읽어올 개수)
//        int guestLimits = 10;
//        //현재지정한 페이지를 지정한 개수만큼 읽어오는데 id를 기준으로 내림차순
//        Pageable salespage = PageRequest.of(currentPage, guestLimits,
//                Sort.by(Sort.Direction.DESC,"salesidx"));
//
//        Page<SalesMonth> sales;
//
//        //if문을 이용해서 각 조건에 따른 조회처리
//        if(type != null && !type.trim().isEmpty()) {
//            System.out.println(type);
//            sales = salesMonthRepository.findByType(oneMonthAgo,threeMonthAgo,
//                    sixMonthsAgo,oneYearsAgo, salespage);
//        } else if(sdate != null) {
//            LocalDateTime startDateTime = sdate.atStartOfDay();
//            LocalDateTime endDateTime = startDateTime.plusDays(1);
//
//            sales =  salesMonthRepository.findByModdateBetween(startDateTime,
//                    endDateTime, salespage);
//        } else {
//            sales = salesMonthRepository.findAll(salespage);
//        }
//
//        //데이터값 변환(ModelMapper DTO<->Entity), Page에 대한 변환X
//
//        Page<SalesMonthDTO> salesDTOS = sales.map(data->modelMapper.map(data,
//                SalesMonthDTO.class));
//        /*Page<PageDTO> guestDTOS = guestEntities.map(
//                data->PageDTO.builder()
//                        .id(data.getId()).guest(data.getPage())
//                        .detail(data.getDetail()).price(data.getPrice())
//                        .quantity(data.getQuantity())
//                        .build()); */
//        return salesDTOS;
//    }

    //개별조회
    public SalesMonthDTO read(Long id) {
        Optional<SalesMonth> salesMonth = salesMonthRepository.findById(id);
            //SalesMonthDTO result = modelMapper.map(storeMember, SalesMonthDTO.class);
            SalesMonthDTO result = salesMonth.map(data->modelMapper.map(data, SalesMonthDTO.class)).orElse(null);

            return result;
    }
}

