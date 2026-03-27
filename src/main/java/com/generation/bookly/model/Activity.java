package com.generation.bookly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "category", length = 50)
    private String category;

    // Gabri: unique = true perché ogni owner può registrare una sola attività.
    // Il vincolo è enforced a livello DB: un secondo insert per lo stesso owner_id
    // restituisce un errore prima ancora di arrivare al service.
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TypeStatus type;
    public enum TypeStatus { PUBLIC, PRIVATE }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ActivityStatus status;
    public enum ActivityStatus { APPROVED, PENDING, REFUSED, BANNED }

    @Column(name = "companyEmail", length = 255)
    private String companyEmail;

    @Column(name = "Registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> calendars;
}