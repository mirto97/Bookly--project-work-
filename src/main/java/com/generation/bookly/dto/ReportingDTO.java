package com.generation.bookly.dto;

import java.time.LocalDate;
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

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Reason is required")
    private String reason;

    @NotNull(message = "Type is required")
    private String type;

    @NotNull(message = "Created date is required")
    private LocalDate createdAt;
}
