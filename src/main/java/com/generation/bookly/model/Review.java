package com.generation.bookly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review 
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Content is required")
    private String content;

    @Min(1) @Max(5)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "booking_id", unique = true, nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    // abbiamo detto che può anche essere null

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;
    public enum ReviewStatus { PENDING, APPROVED, REJECTED }
}
