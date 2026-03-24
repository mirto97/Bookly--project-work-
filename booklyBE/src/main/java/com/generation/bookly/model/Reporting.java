package com.generation.bookly.model;

import java.time.LocalDate;

import jakarta.persistence.*;
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

    @Column(name = "reference", nullable = false)
    private int referenceId;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportingStatus status = ReportingStatus.PENDING;

    private LocalDate createdAt;

}