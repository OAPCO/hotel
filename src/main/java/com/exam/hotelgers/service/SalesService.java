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
        Sales sales = modelMapper.map(salesDTO, Sales.class);
        Sales result = salesRepository.save(sales);

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
                salesDTO.getStorename(),  //매장명
                salesDTO.getPayment_method(), //결재방식
                salesDTO.getProcessing_status(), //처리상태
                salesDTO.getDistributor_organization(),//총판조직
                salesDTO.getBranch(),//지사
                salesDTO.getStore(),//매장
                salesDTO.getStartDate(),//시작일
                salesDTO.getEndDate(),//종료일
                pageable);


        Page<SalesDTO> result = salesEntities.map(data->modelMapper.map(data,SalesDTO.class));

        return result;
    }

    //개별조회
    public SalesDTO read(Long id) {
        Optional<Sales> storeMember = salesRepository.findById(id);
            //SalesDTO result = modelMapper.map(storeMember, SalesDTO.class);
            SalesDTO result = storeMember.map(data->modelMapper.map(data, SalesDTO.class)).orElse(null);

            return result;
    }
}

