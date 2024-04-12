package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreMemberDTO;
import com.exam.hotelgers.entity.StoreMember;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.StoreMemberRepository;
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
public class StoreMemberService {
    private final StoreMemberRepository storeMemberRepository;
    private final ModelMapper modelMapper;

    //삽입
    public StoreMember insert(StoreMemberDTO storeMemberDTO) {
        StoreMember storeMember = modelMapper.map(storeMemberDTO, StoreMember.class);
        StoreMember result = storeMemberRepository.save(storeMember);

        return result;
    }

    //수정
    public StoreMemberDTO update(StoreMemberDTO storeMemberDTO) {
        Optional<StoreMember> search = storeMemberRepository.findById(storeMemberDTO.getStoreMemberIdx());

        if(search.isPresent()) {
            StoreMember storeMember = modelMapper.map(storeMemberDTO, StoreMember.class);
            storeMemberRepository.save(storeMember);
        }
        StoreMemberDTO result = search.map(data ->modelMapper.map(data, StoreMemberDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long id) {
        storeMemberRepository.deleteById(id);
    }

    //전체조회
    public Page<StoreMemberDTO> select(Pageable page, SearchDTO searchDTO) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                            Sort.by(Sort.Direction.DESC,"storeMemberIdx"));

        Page<StoreMember> memberEntities = storeMemberRepository.search(
                searchDTO.getStoreMemberEmail(),
                searchDTO.getStoreMemberName(),
                searchDTO.getStoreMemberTel(),
                searchDTO.getStoreMemberState(),
                searchDTO.getStoreMemberAuth(),
                searchDTO.getStoreDistributorIdx(),
                searchDTO.getStoreBranchIdx(),
                searchDTO.getStoreIdx(),
                pageable);


        Page<StoreMemberDTO> result = memberEntities.map(data->modelMapper.map(data,StoreMemberDTO.class));

        return result;
    }

    //개별조회
    public StoreMemberDTO read(Long id) {
        Optional<StoreMember> storeMember = storeMemberRepository.findById(id);
            //StoreMemberDTO result = modelMapper.map(storeMember, StoreMemberDTO.class);
            StoreMemberDTO result = storeMember.map(data->modelMapper.map(data, StoreMemberDTO.class)).orElse(null);

            return result;
    }
}

