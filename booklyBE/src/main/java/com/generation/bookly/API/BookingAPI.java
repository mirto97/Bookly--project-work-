package com.generation.bookly.API;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingAPI {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> create(@RequestBody BookingDTO dto) {
        return new ResponseEntity<>(bookingService.createBooking(dto), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getByUser(@PathVariable int userId) {
        // Supponendo di avere un metodo findByUser nel Service
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
}