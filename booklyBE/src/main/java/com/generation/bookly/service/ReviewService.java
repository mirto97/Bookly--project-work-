package com.generation.bookly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ReviewDTO;
import com.generation.bookly.mapper.ReviewMapper;
import com.generation.bookly.model.Review;
import com.generation.bookly.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO postReview(ReviewDTO dto) {
        Review review = reviewMapper.toEntity(dto);
        review.setStatus(Review.ReviewStatus.PENDING); // Moderazione preventiva
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    @Transactional
    public void updateReviewStatus(int reviewId, Review.ReviewStatus status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        review.setStatus(status);
        reviewRepository.save(review);
    }

    public List<ReviewDTO> getApprovedReviewsByActivity(Integer activityId) {
        // Recupera solo le recensioni APPROVED per una specifica attività
        return reviewRepository.findByBookingServiceActivityIdAndStatus(activityId, Review.ReviewStatus.APPROVED)
                .stream()
                .map(reviewMapper::toDTO)
                        .collect(Collectors.toList());
    }
}