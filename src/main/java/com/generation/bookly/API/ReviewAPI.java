package com.generation.bookly.API;

import com.generation.bookly.dto.ReviewDTO;
import com.generation.bookly.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/bookly/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewAPI 
{

    private final ReviewService reviewService;

    // qua me l'ha fatto che se passo activityId mi restituisce solo quelle, se passo userId solo quelle, altrimenti tutte
    // valutiamo se ha senso
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll(
            @RequestParam(required = false) Integer activityId,
            @RequestParam(required = false) Integer userId) 
    {
        if (activityId != null) {
            return ResponseEntity.ok(reviewService.getReviewsByActivityId(activityId));
        } else if (userId != null) {
            return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
        } else {
            return ResponseEntity.ok(reviewService.getAll());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReviewDTO> updateStatus(@PathVariable int id, @RequestParam String status) {
        return ResponseEntity.ok(reviewService.updateStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable int id) {
        ReviewDTO review = reviewService.getById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) 
    {
        return ResponseEntity.ok(reviewService.postReview(dto));
    }

    // Tutte le recensioni di un'attività specifica
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ReviewDTO>> getByActivity(@PathVariable Integer activityId) 
    {
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