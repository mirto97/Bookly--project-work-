package com.generation.bookly.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.generation.bookly.model.Booking;
import com.generation.bookly.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Component("bookingSecurity")
@RequiredArgsConstructor
public class BookingSecurity {

    private final BookingRepository bookingRepository;

    public boolean isBookingOwner(Integer bookingId, Authentication authentication) {
        if (bookingId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        return bookingRepository.findById(bookingId)
                .map(booking -> sameEmail(booking.getUser() != null ? booking.getUser().getEmail() : null, currentUserEmail))
                .orElse(false);
    }

    public boolean isOwnerOfBookingActivity(Integer bookingId, Authentication authentication) {
        if (bookingId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        return bookingRepository.findById(bookingId)
                .map(booking -> {
                    Booking b = booking;
                    if (b.getService() == null || b.getService().getActivity() == null || b.getService().getActivity().getOwner() == null) {
                        return false;
                    }
                    return sameEmail(b.getService().getActivity().getOwner().getEmail(), currentUserEmail);
                })
                .orElse(false);
    }

    private boolean sameEmail(String a, String b) {
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }
}

