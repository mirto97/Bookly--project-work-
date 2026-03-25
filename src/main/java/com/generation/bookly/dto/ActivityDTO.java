package com.generation.bookly.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private int id;

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Type is required")
    private String type;

    // per ora li passiamo tramite stringa anche se sono enum, così evitiamo problemi di serializzazione

    @NotBlank(message = "Activity status is required")
    private String activityStatus;

    @NotBlank(message = "Company email is required")
    private String companyEmail;

    @NotBlank(message = "Registration Date is required")
    private LocalDateTime registrationDate;

    private List<ServiceDTO> services;

    private List<DiaryDTO> calendars;

}
