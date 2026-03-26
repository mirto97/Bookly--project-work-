package com.generation.bookly.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDTO 
{
    
    private Integer id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Start hour is required")
    private LocalTime startHour;

    @NotNull(message = "End hour is required")
    private LocalTime endHour;

    @NotNull(message = "Activity ID is required")
    private Integer activityId;

    @NotBlank(message = "Available status is required")
    private String available;

    private List<BookingDTO> bookings;
}
