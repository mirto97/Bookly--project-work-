package com.generation.bookly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    // Impostato server-side al momento della creazione.
    // @JsonFormat garantisce la serializzazione come ISO string indipendentemente
    // dalla configurazione globale di Jackson — il frontend può fare new Date(createdAt)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private String ownerReply;
}
