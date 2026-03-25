package com.generation.bookly.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private int id;

    @NotNull(message = "Guest ID is required")
    private int authorId;
    
    @NotBlank(message = "Content is required")
    private String content;

    @Min(1) @Max(5)
    private int rating;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Booking ID is required")
    private int bookingId;
    
}
