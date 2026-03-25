package com.generation.bookly.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    
    private int id;

    @NotNull(message = "Diary ID is required")
    private int diaryId;

    @NotNull(message = "Guest ID is required")
    private int guestId;

    @NotNull(message = "Service ID is required")
    private int serviceId;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    private String note;

    private List<ReviewDTO> reviews;
}
