package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.DistChiefDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class DistChiefService {

    private final DistChiefRepository distChiefRepository;
    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final DistRepository distRepository;



    public Long register(DistChiefDTO distChiefDTO){


        List<String> adminIdCheck = adminRepository.registerCheck(distChiefDTO.getDistChiefId());
        List<String> distChiefIdCheck = distChiefRepository.registerCheck(distChiefDTO.getDistChiefId());
        List<String> managerIdCheck = managerRepository.registerCheck(distChiefDTO.getDistChiefId());


        if(!adminIdCheck.isEmpty() || !distChiefIdCheck.isEmpty() || !managerIdCheck.isEmpty()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }

        String password = passwordEncoder.encode(distChiefDTO.getPassword());
        DistChief distChief = modelMapper.map(distChiefDTO, DistChief.class);



        if(distChiefDTO.getRoleType().equals(RoleType.DISTCHIEF)){
            distChief.setRoleType(RoleType.DISTCHIEF);
        }

        distChief.setPassword(password); //비밀번호는 암호화된 비밀번호로 변경처리

        Long distChiefIdx = distChiefRepository.save(distChief).getDistChiefIdx();

        return distChiefIdx;
    }





    public void delete(Long distChiefIdx){
        distChiefRepository.deleteById(distChiefIdx);
    }


    public DistChiefDTO read(Long distChiefIdx){

        Optional<DistChief> temp= distChiefRepository.findById(distChiefIdx);


        return modelMapper.map(temp,DistChiefDTO.class);
    }


    public Page<DistChiefDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"distChiefIdx"));

        Page<DistChief> distChiefs = distChiefRepository.findAll(page);


        Page<DistChiefDTO> DistChiefDTOS = distChiefs.map(data->modelMapper.map(data,DistChiefDTO.class));

        return DistChiefDTOS;
    }



    public List<DistDTO> distChiefOfDistList(Principal principal) {

        String userId = principal.getName();
        List<Dist> dists = distRepository.findByDistChief_DistChiefId(userId);

        return dists.stream()
                .map(dist -> modelMapper.map(dist, DistDTO.class))
                .collect(Collectors.toList());
    }




//    public Page<DistChiefDTO> distChiefSearch(SearchDTO searchDTO, Pageable pageable) {
//
//        int currentPage = pageable.getPageNumber() - 1;
//        int pageCnt = 5;
//        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distChiefIdx"));
//
//        Page<DistChief> distChiefs = distChiefRepository.distChiefSearch(searchDTO, page);
//        return distChiefs.map(this::convertToDTO);
//    }


    private DistChiefDTO convertToDTO(DistChief distChief) {
        DistChiefDTO dto = modelMapper.map(distChief, DistChiefDTO.class);
        return dto;
    }

    @Transactional
    public boolean changePassword(String currentPassword, String newPassword, Principal principal) {
        String userId = principal.getName();
        DistChief distChief =distChiefRepository.findByDistChiefId(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        int result;
        // 현재 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, distChief.getPassword())) {
            log.info("비밀번호 일치하지 않음. 입력된 비밀번호: " + currentPassword + " 저장된 비밀번호: " + distChief.getPassword());
            return false; // 현재 비밀번호가 일치하지 않음
        }

        // 새로운 비밀번호로 업데이트
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        distChief.setPassword(encodedNewPassword);
        distChiefRepository.save(distChief);
        log.info("패스워드 변경 성공. 새로운 비밀번호: " + encodedNewPassword);
        return true; // 비밀번호 변경 성공
    }
}
