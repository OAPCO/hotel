package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.ReviewDTO;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Review;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.ReviewRepository;
import com.exam.hotelgers.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewService {
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberrepository;

    public void register(ReviewDTO reviewDTO) throws Exception {
        log.info("ReviewDTO received in service: " + reviewDTO.toString());

        // Store 객체 찾기
        Store store = storeRepository.findById(reviewDTO.getStoreIdx())
                .orElseThrow(() -> new Exception("Store not found"));

        // Member 객체 찾기
        Member member = memberrepository.findByMemberIdx(reviewDTO.getMemberIdx())
                .orElseThrow(() -> new Exception("Store not found"));

        // ReviewDTO를 Review 엔티티로 매핑
        Review review = modelMapper.map(reviewDTO, Review.class);

        // Store 설정
        review.setStore(store);
        review.setMember(member);

        // Review 저장
        reviewRepository.save(review);

        log.info("Review saved: " + review.toString());
    }
    public List<ReviewDTO> findByStoreIdx(Long storeIdx) {
        List<Review> reviews = reviewRepository.findByStoreIdx(storeIdx);
        return reviews.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
                    // 리뷰 테이블에 있는 memberIdx를 사용하여 멤버 정보 가져오기
                    Member member = review.getMember();
                    if (member != null) {
                        // 멤버 정보가 있다면 DTO에 이메일 설정
                        reviewDTO.setMemberEmail(member.getMemberEmail());
                    }
                    return reviewDTO;
                })
                .collect(Collectors.toList());
    }

    public double calculateAverageRating(List<ReviewDTO> reviewDTOList) {
        OptionalDouble average = reviewDTOList.stream()
                .mapToDouble(reviewDTO -> reviewDTO.getRate().ordinal() + 1)
                .average();
        return average.orElse(0);
    }
}
