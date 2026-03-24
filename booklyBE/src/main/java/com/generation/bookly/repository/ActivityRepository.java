package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    
}
