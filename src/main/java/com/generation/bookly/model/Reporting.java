package com.generation.bookly.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reporting")
public class Reporting 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    private ReportingType type;
    public enum ReportingType { REVIEW, CLIENT, ACTIVITY }
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private ReportingStatus status = ReportingStatus.PENDING;
    public enum ReportingStatus { PENDING, APPROVED, REJECTED }
    
    @NotNull(message = "Created date is required")
    private LocalDateTime createdAt = LocalDateTime.now();
    
}