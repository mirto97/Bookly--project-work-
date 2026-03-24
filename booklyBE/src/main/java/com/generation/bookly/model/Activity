package com.generation.bookly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.generation.bookly.model.Calendar;

@Entity
@Table(name = "activity")
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
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Services> services = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calendar> calendars = new ArrayList<>();

    public Activity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TypeStatus getType() {
        return type;
    }

    public void setType(TypeStatus type) {
        this.type = type;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Servizio> getService() {
        return services;
    }

    public void setServices(List<Servizio> services) {
        this.services = services;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }
}