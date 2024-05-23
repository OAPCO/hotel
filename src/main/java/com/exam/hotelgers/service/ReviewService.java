package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.ReviewDTO;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Review;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.ReviewRepository;
import com.exam.hotelgers.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewService {
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    public void register(ReviewDTO reviewDTO) throws Exception{
        Store store = storeRepository.findById(reviewDTO.getStore().getStoreIdx())
                .orElseThrow(() -> new Exception("Store not found"));

        reviewDTO.setStore(store);

        Review review = modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
    }

}
