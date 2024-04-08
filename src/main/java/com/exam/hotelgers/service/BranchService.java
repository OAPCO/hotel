package com.exam.hotelgers.service;


import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.branchDTO;
import com.exam.hotelgers.dto.distDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.repository.BranchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BrandRepository brandRepository;


    public Long register(branchDTO branchDTO) {


//        Optional<Dist> dist = distRepository.findByStoreDistIdx(branchDTO.getStoreDistDTO().getStoreDistIdx());
        Optional<Dist> dist = distRepository.findByDistCd(branchDTO.getDistDTO().getDistCd());
        Optional<Brand> brand = brandRepository.findByBrandCd(branchDTO.getBrandDTO().getBrandCd());

        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }

        if (!brand.isPresent()) {
            throw new IllegalStateException("존재하지 않는 브랜드 코드입니다.");
        }



        Optional<Branch> branchOptional = branchRepository
                .findByBranchCd(branchDTO.getBranchCd());

        if(branchOptional.isPresent()) {
            throw new IllegalStateException("이미 있는 코드입니다.");
        }

        Branch branch = modelMapper.map(branchDTO, Branch.class);

        branch.setDist(dist.get());
        branch.setBrand(brand.get());


        branchRepository.save(branch);

        return branchRepository.save(branch).getBranchIdx();
    }


    public void modify(branchDTO branchDTO){


        Optional<Branch> temp = branchRepository
                .findByBranchIdx(branchDTO.getBranchIdx());

        if(temp.isPresent()) {

            Branch branch = modelMapper.map(branchDTO, Branch.class);
            branchRepository.save(branch);
        }

    }

    public branchDTO read(Long storeIdx){

        Optional<Branch> branch= branchRepository.findById(storeIdx);


        return modelMapper.map(branch,branchDTO.class);
    }


    public Page<branchDTO> list(Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "branchIdx"));

        Page<Branch> stores = branchRepository.findAll(page);
        return stores.map(this::convertToDTO);
    }

    private branchDTO convertToDTO(Branch branch) {
        branchDTO dto = modelMapper.map(branch, branchDTO.class);
        dto.setDistDTO(convertToStoreDistDTO(branch.getDist()));
        dto.setBrandDTO(convertToBrandDTO(branch.getBrand()));
        return dto;
    }

    private distDTO convertToStoreDistDTO(Dist dist) {
        return modelMapper.map(dist, distDTO.class);
    }

    private BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }



    public void delete(Long storeIdx){
        branchRepository.deleteById(storeIdx);
    }
}
