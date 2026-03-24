package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Booking;

public interface BookingRepository  extends JpaRepository<Booking, Integer> {

}
