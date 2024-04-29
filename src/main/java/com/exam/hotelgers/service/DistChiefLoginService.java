package com.exam.hotelgers.service;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.repository.DistChiefRepository;
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
public class DistChiefLoginService implements UserDetailsService {


    @Autowired
    private DistChiefRepository distChiefRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) {
        Optional<DistChief> distChiefEntity = distChiefRepository.findByDistChiefId(userid);
        if (distChiefEntity.isPresent()) {

            log.info("총판장 로그인서비스 들어옴");

            return User.withUsername(distChiefEntity.get().getDistChiefId())
                    .password(distChiefEntity.get().getPassword())
                    .roles(distChiefEntity.get().getRoleType().name())
                    .build();
        }
        throw new UsernameNotFoundException("알 수 없는 아이디 : "+ userid);
    }
}