package com.generation.bookly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Booking;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findByStatus(Activity.ActivityStatus status);
    List<Activity> findByCategory(String category);  
}
