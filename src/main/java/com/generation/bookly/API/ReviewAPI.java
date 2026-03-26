package com.generation.bookly.API;

import com.generation.bookly.dto.ReviewDTO;
import com.generation.bookly.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookly/api/reviews")
@RequiredArgsConstructor
public class ReviewAPI {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.postReview(dto));
    }

    // Tutte le recensioni di un'attività specifica
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ReviewDTO>> getByActivity(@PathVariable Integer activityId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsByActivity(activityId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/content")
    public ResponseEntity<Void> updateReview(@PathVariable int id, @RequestBody String content) {
        reviewService.updateContent(id, content);
        return ResponseEntity.noContent().build();
    }

    // tutte le recensioni di un utente specifico (opzionale, ma utile per moderazione?)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getByUser(@PathVariable Integer userId) {
        // Implementa un metodo nel ReviewService per recuperare le recensioni di un utente
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}