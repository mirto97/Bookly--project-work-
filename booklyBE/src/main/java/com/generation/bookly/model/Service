package com.generation.bookly.model;    

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "price", precision = 10, scale = 2)
    private double price;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "personalized_fields", columnDefinition = "json")
    private Map<String, Object> personalizedFields;

    public Service() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Object> getPersonalizedFields() {
        return personalizedFields;
    }

    public void setPersonalizedFields(Map<String, Object> personalizedFields) {
        this.personalizedFields = personalizedFields;
    }
}