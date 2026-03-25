package com.generation.bookly.API;

import com.generation.bookly.dto.ReviewDTO;
import com.generation.bookly.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookly/api/reviews")
@RequiredArgsConstructor
public class ReviewAPI {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.postReview(dto));
    }

    // Tutte le recensioni di un'attività specifica
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ReviewDTO>> getByActivity(@PathVariable Integer activityId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsByActivity(activityId));
    }
}