package com.generation.bookly.model;    

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    @Min(value=0, message="Duration cannot be negative")
    private int duration;

    @Column(name = "price")
    @Min(value=0, message="Price cannot be negative")
    private int price;

    // meglio non complicarci la vita ora
    // @JdbcTypeCode(SqlTypes.JSON)
    // @Column(name = "personalized_fields", columnDefinition = "json")
    // private Map<String, Object> personalizedFields;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    private String notes;
}