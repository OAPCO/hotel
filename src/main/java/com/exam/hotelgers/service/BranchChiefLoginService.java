package com.exam.hotelgers.service;
import com.exam.hotelgers.entity.BranchChief;
import com.exam.hotelgers.repository.BranchChiefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log
public class BranchChiefLoginService implements UserDetailsService {
    @Autowired
    private BranchChiefRepository branchChiefRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) {
        Optional<BranchChief> branchChiefEntity = branchChiefRepository.findByBranchChiefId(userid);
        if (branchChiefEntity.isPresent()) {

            log.info("지사장 로그인서비스 들어옴");

            return User.withUsername(branchChiefEntity.get().getBranchChiefId())
                    .password(branchChiefEntity.get().getPassword())
                    .roles(branchChiefEntity.get().getRoleType().name())
                    .build();
        }
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}