package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    
}
