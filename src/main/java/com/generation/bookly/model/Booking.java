package com.generation.bookly.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// NON usiamo @Data su entity JPA!
// @Data genera equals()/hashCode() basati su TUTTI i campi, incluse le relazioni lazy.
// Questo causa problemi con Hibernate: quando accede a una relazione non ancora caricata
// (lazy proxy), può lanciare LazyInitializationException o produrre loop infiniti.
// Regola: su @Entity usare sempre @Getter + @Setter separati.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking 
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    public enum BookingStatus { PENDING, CONFIRMED, CANCELLED, COMPLETED }
    // CONFIRMED non lo usiamo più ma lo lascio per eventuali estensioni future

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate = LocalDate.now();

    private String note;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Review> reviews;

}
