package com.generation.bookly.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO 
{

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 0, message = "Price cannot be negative")
    private int price;

    @Min(value = 0, message = "Duration cannot be negative")
    private int duration;

    @NotNull(message = "Service status is required")
    private Integer activityId;

    private List<BookingDTO> bookings;

    private String notes;
}
