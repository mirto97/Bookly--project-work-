package com.generation.bookly.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User 
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // "GUEST" o "ADMIN"
    public enum Role { GUEST, ADMIN, OWNER }

    @Column(name = "reportings_received")
    @Min(value = 0, message = "Reportings received cannot be negative")
    private int reportingsReceived = 0;

    // Aggiunto per la pagina profilo del guest: l'utente può scrivere una breve bio su se stesso.
    // TEXT invece di VARCHAR perché la bio può essere più lunga di 255 caratteri.
    @Column(columnDefinition = "TEXT")
    private String bio;

    // URL della foto profilo: il client carica l'immagine su un hosting esterno (es. imgur, cloudinary)
    // e salva solo l'URL qui. Evita di stoccare binari nel DB.
    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
