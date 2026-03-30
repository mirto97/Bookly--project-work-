package com.generation.bookly.service;

import java.time.LocalDateTime;
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
public class ReviewService 
{

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewDTO> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO updateStatus(int id, String status) {
        // Controllo esistenza
        reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review non trovata: " + id));

        // Validazione e conversione dello status
        Review.ReviewStatus newStatus;
        try {
            newStatus = Review.ReviewStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status non valido: " + status);
        }
        updateReviewStatus(id, newStatus);
        return getById(id);
    }

    @Transactional
    public ReviewDTO postReview(ReviewDTO dto) 
    {
        Review review = reviewMapper.toEntity(dto);
        review.setStatus(Review.ReviewStatus.PENDING); // Moderazione preventiva
        review.setCreatedAt(LocalDateTime.now());
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    @Transactional
    public void updateReviewStatus(int reviewId, Review.ReviewStatus status) 
    {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        review.setStatus(status);
        reviewRepository.save(review);
    }

    public List<ReviewDTO> getApprovedReviewsByActivity(Integer activityId) 
    {
        // Recupera solo le recensioni APPROVED per una specifica attività
        return reviewRepository.findByBookingServiceActivityIdAndStatus(activityId, Review.ReviewStatus.APPROVED)
                .stream()
                .map(reviewMapper::toDTO)
                        .collect(Collectors.toList());
    }

    public void deleteById(int id) {
        reviewRepository.deleteById(id);
    }

    @Transactional
    public void updateContent(int id, String content) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        
        review.setStatus(Review.ReviewStatus.PENDING); // Moderazione preventiva        
        review.setContent(content);
        reviewRepository.save(review);
    }

    // Metodo per recuperare le recensioni di un utente specifico
    public List<ReviewDTO> getReviewsByUserId(Integer userId) {
        return reviewRepository.findByAuthorId(userId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByActivityId(Integer activityId) {
        return reviewRepository.findByBooking_Service_Activity_Id(activityId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getById(int id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toDTO)
                .orElse(null);
    }

    public List<ReviewDTO> getPendingReviews() {
        return reviewRepository.findByStatus(Review.ReviewStatus.PENDING)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO addReply(int reviewId, String replyText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata"));
        
        review.setOwnerReply(replyText);
        return reviewMapper.toDTO(reviewRepository.save(review));
    }
}