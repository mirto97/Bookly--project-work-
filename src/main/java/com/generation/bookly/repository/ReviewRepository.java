package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> 
{
    // Trova le recensioni approvate per un'attività (passando dai servizi)
    List<Review> findByBookingServiceActivityIdAndStatus(Integer activityId, Review.ReviewStatus status);
}
