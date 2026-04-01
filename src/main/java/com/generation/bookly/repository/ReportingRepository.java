package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Reporting;

public interface ReportingRepository extends JpaRepository<Reporting, Integer> 
{
    List<Reporting> findByStatus(Reporting.ReportingStatus status);

    // Controlla se esiste già un report per quel booking fatto da quell'owner
    boolean existsByBookingIdAndAuthorId(int bookingId, int authorId);
}
