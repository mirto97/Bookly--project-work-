package com.generation.bookly.API;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.model.Booking;
import com.generation.bookly.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//gabri
@RestController
@RequestMapping("/bookly/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookingAPI {

    private final BookingService bookingService;

    // @Valid attiva la validazione delle annotazioni nel DTO (es. @NotNull, @Min...)
    // SENZA @Valid, Spring ignora completamente le annotazioni di validazione nel DTO!
    @PostMapping
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingDTO dto) {
        return new ResponseEntity<>(bookingService.createBooking(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> findAll() {
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    // @Valid anche qui: l'update riceve un DTO completo, vogliamo validarlo allo stesso modo
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateById(@PathVariable int id, @Valid @RequestBody BookingDTO dto) {
        return ResponseEntity.ok(bookingService.updateBookingById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateStatusById(@PathVariable int id, @RequestParam Booking.BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateBookingStatusById(id, status));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<BookingDTO>> findByActivityId(@PathVariable int activityId)
    {
        return ResponseEntity.ok(bookingService.getByActivityId(activityId));
    }
}
