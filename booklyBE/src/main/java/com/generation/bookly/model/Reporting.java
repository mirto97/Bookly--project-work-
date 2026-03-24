package com.generation.bookly.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporting 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Guest author;

    @Enumerated(EnumType.STRING)
    private ReportingType type;
    public enum ReportingType { REVIEW, CLIENT, ACTIVITY;}
    
    @Column(name = "reference", nullable = false)
    private int referenceId;
    
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private ReportingStatus status = ReportingStatus.PENDING;
    public enum ReportingStatus { PENDING, APPROVED, REJECTED;}
    
    @NotNull
    private LocalDate createdAt;
    
}