package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Reporting;

public interface ReportingRepository extends JpaRepository<Reporting, Integer> {
    
}
