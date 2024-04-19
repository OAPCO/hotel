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
            SalesMonth salesMonth = modelMapper.map(salesMonthDTO, SalesMonth.class);
            salesMonthRepository.save(salesMonth);
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

    //개별조회
    public SalesMonthDTO read(Long id) {
        Optional<SalesMonth> salesMonth = salesMonthRepository.findById(id);
            //SalesMonthDTO result = modelMapper.map(storeMember, SalesMonthDTO.class);
            SalesMonthDTO result = salesMonth.map(data->modelMapper.map(data, SalesMonthDTO.class)).orElse(null);

            return result;
    }
}

