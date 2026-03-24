package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    
}
