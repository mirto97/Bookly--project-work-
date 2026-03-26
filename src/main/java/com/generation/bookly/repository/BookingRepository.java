package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Spring Data JPA deriva le query dal nome del metodo ("query derivation").
    // Quando il campo è una relazione @ManyToOne (es. "private User user"),
    // bisogna navigare fino all'ID con il separatore underscore "_".
    //
    // SENZA underscore: findByUserId → Spring prova a trovare un campo "userId" diretto → ERRORE
    // CON underscore:   findByUser_Id → Spring capisce "user" (relazione) e poi "id" (campo) → OK
    //
    // Lo stesso principio vale per findByUser_IdAndStatus: naviga user.id E filtra per status.
    List<Booking> findByUser_Id(int userId);

    // findByStatus funziona direttamente perché "status" è un campo semplice (enum) sull'entity
    List<Booking> findByStatus(Booking.BookingStatus status);

    // Naviga: service → activity → id
    List<Booking> findByService_Activity_Id(int activityId);

    // Combinazione di due filtri: user.id E status
    List<Booking> findByUser_IdAndStatus(int userId, Booking.BookingStatus status);

    // Combinazione: service.activity.id E status
    List<Booking> findByService_Activity_IdAndStatus(int activityId, Booking.BookingStatus status);
}
