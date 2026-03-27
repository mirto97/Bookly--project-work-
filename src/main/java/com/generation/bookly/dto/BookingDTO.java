package com.generation.bookly.dto;

import java.time.LocalDate;
import java.util.List;

import com.generation.bookly.model.Booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Su DTO possiamo usare @Data liberamente: non è una entity JPA,
// non ha relazioni lazy, quindi equals/hashCode/toString sono sicuri.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Integer id;

    // ATTENZIONE: @NotNull NON funziona su tipi primitivi (int, long, boolean...)
    // perché i primitivi non possono mai essere null per definizione del linguaggio Java.
    // Soluzione: usare il tipo wrapper Integer (oggetto) invece di int (primitivo).
    // Così @NotNull può verificare che il campo sia stato effettivamente valorizzato.
    @NotNull(message = "Diary ID is required")
    private Integer diaryId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Service ID is required")
    private Integer serviceId;

    // Campo read-only, derivato da service.activity.id via @Mapping nel BookingMapper.
    // Il frontend ne ha bisogno per sapere a quale attività appartiene la prenotazione,
    // così può caricare gli slot disponibili (GET /diary/available/{activityId}) durante il reschedule.
    // Non viene mai inviato dal client; viene ignorato in toEntity() perché Booking non ha questo campo.
    private Integer activityId;

    // Usiamo l'enum BookingStatus invece di String:
    // - più sicuro (impedisce valori non validi come "PIPPO")
    // - coerente con l'entità Booking
    // - MapStruct converte automaticamente enum ↔ String se necessario
    // @NotNull invece di @NotBlank perché @NotBlank funziona solo su String
    @NotNull(message = "Status is required")
    private Booking.BookingStatus status;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    private String note;

    private List<ReviewDTO> reviews;
}
