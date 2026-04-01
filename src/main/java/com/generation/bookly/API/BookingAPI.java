package com.generation.bookly.API;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.model.Booking;
import com.generation.bookly.model.User;
import com.generation.bookly.repository.UserRepository;
import com.generation.bookly.service.BookingService;
import com.generation.bookly.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//gabri
@RestController
@RequestMapping("/bookly/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookingAPI {

    private final BookingService bookingService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    // @Valid attiva la validazione delle annotazioni nel DTO (es. @NotNull, @Min...)
    // SENZA @Valid, Spring ignora completamente le annotazioni di validazione nel DTO!

    @GetMapping
    public ResponseEntity<List<BookingDTO>> findAll() {
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#userId, authentication)")
    public ResponseEntity<List<BookingDTO>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    // @Valid anche qui: l'update riceve un DTO completo, vogliamo validarlo allo stesso modo
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")   
    public ResponseEntity<BookingDTO> updateById(@PathVariable int id, @Valid @RequestBody BookingDTO dto) {
        return ResponseEntity.ok(bookingService.updateBookingById(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<BookingDTO> updateStatusById(@PathVariable int id, @RequestParam Booking.BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateBookingStatusById(id, status));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<BookingDTO>> findByActivityId(@PathVariable int activityId)
    {
        return ResponseEntity.ok(bookingService.getByActivityId(activityId));
    }
    
    @PostMapping
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingDTO dto) {
        // 1. Salviamo la prenotazione tramite il service
        BookingDTO savedBooking = bookingService.createBooking(dto);

        // 2.1 Recupera i dettagli dell'utente per la mail
        User user = userRepository.findById(dto.getUserId())
                  .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        // 2.2 Inviamo la mail di conferma
        // Assicurati che BookingDTO contenga il campo email o recuperalo dall'oggetto salvato
        try {
            emailService.inviaMailConferma(
                user.getEmail(), // o come si chiama il campo nel tuo DTO
                user.getFirstName(), 
                "Dettagli: Attività ID " + savedBooking.getActivityId() + " per il giorno " + savedBooking.getBookingDate()
            );
        } catch (Exception e) {
            // Logghiamo l'errore ma non blocchiamo la risposta 201 
            // perché la prenotazione nel DB è comunque avvenuta con successo
            System.err.println("Errore invio mail: " + e.getMessage());
        }

        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }
}
