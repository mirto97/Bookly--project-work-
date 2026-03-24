package com.generation.bookly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.generation.bookly.model.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.generation.bookly.model.Service;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "category", length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TypeStatus type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ActivityStatus status;

    @Column(name = "companyEmail", length = 255)
    private String companyEmail;

    @Column(name = "Registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calendar> calendars = new ArrayList<>();
}