package com.generation.bookly.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diary")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Diary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Start hour is required")
    private LocalTime startHour;

    @NotNull(message = "End hour is required")
    private LocalTime endHour;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    private Available available = Available.AVAILABLE;
    public enum Available { AVAILABLE, NOTAVAILABLE, BOOKED }
}