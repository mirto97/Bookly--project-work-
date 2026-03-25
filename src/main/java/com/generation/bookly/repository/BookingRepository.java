package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Booking;

public interface BookingRepository  extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(int userId);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByServiceActivityId(int activityId);
    List<Booking> findByUserIdAndStatus(int userId, Booking.BookingStatus status);
    List<Booking> findByServiceActivityIdAndStatus(int activityId, Booking.BookingStatus status);
}
