package com.generation.bookly.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO 
{

    private Integer id;

    @NotNull(message = "Guest ID is required")
    private Integer authorId;

    @NotBlank(message = "Content is required")
    private String content;

    @Min(1) @Max(5)
    private int rating;

    // Impostato server-side: il client non lo invia, viene restituito nella risposta
    private String status;

    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    // Impostato server-side al momento della creazione
    private LocalDateTime createdAt;
}
