package com.generation.bookly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> 
{
    List<Activity> findByStatus(Activity.ActivityStatus status);
    List<Activity> findByCategory(String category);  
    Optional<Activity> findByOwner_Id(Integer ownerId);
}
