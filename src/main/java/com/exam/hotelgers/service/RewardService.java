package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.RewardDTO;
import com.exam.hotelgers.entity.Reward;
import com.exam.hotelgers.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RewardService {
    private final RewardRepository rewardRepository;
    private final ModelMapper modelMapper;


    public Long register(RewardDTO rewardDTO) {
        Reward reward = modelMapper.map(rewardDTO, Reward.class);
        Reward savedReward = rewardRepository.save(reward);
        return savedReward.getRewardIdx();
    }


}
