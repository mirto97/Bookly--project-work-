package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    // Trova tutti i servizi offerti da una specifica attività
    List<Service> findByActivityId(Integer activityId);    
}
