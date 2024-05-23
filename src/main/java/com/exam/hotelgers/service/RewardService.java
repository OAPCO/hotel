package com.exam.hotelgers.service;

import com.exam.hotelgers.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
@Log4j2
public class RewardService {
    private final RewardRepository rewardRepository;
    private final ModelMapper modelMapper;
}
