package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Reporting;

public interface ReportingRepository extends JpaRepository<Reporting, Integer> 
{
    List<Reporting> findByStatus(Reporting.ReportingStatus status);
}
