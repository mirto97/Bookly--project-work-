package com.generation.bookly.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO 
{

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Category is required")
    private String category;

    // Gabri: @NotBlank è valido solo su String/CharSequence, non su Integer.
    // Sostituito con @NotNull per evitare ConstraintDefinitionException a runtime.
    @NotNull(message = "Owner ID is required")
    private Integer ownerId;

    @NotBlank(message = "Type is required")
    private String type;

    // per ora li passiamo tramite stringa anche se sono enum, così evitiamo problemi di serializzazione

    @NotBlank(message = "Activity status is required")
    private String status;


    @NotBlank(message = "Company email is required")
    private String companyEmail;

    // Gabri: registrationDate viene impostata lato backend in ActivityService,
    // quindi il frontend non la invia e non va validata qui.
    private LocalDateTime registrationDate;

    private List<ServiceDTO> services;

    private List<DiaryDTO> calendars;

}
