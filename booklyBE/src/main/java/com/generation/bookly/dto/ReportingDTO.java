package com.generation.bookly.dto;

import java.time.LocalDate;

import com.generation.bookly.model.ReportingStatus;
import com.generation.bookly.model.ReportingType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportingDTO 
{
    private int id;

    @NotNull(message = "Author ID is required")
    private int authorId;

    @Enumerated(EnumType.STRING)
    private ReportingStatus status;

    @NotNull(message = "Reason is required")
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportingType type;

    @NotNull(message = "Created date is required")
    private LocalDate createdAt;
}
