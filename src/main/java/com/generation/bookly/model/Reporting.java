package com.generation.bookly.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reporting", uniqueConstraints = {
    // Impedisce allo stesso autore di segnalare lo stesso booking più di una volta
    @UniqueConstraint(columnNames = {"booking_id", "author_id"})
})
public class Reporting 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    // AGGIUNTO: Relazione con il booking oggetto della segnalazione
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

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