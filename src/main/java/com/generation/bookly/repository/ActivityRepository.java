package com.generation.bookly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> 
{
    List<Activity> findByStatus(Activity.ActivityStatus status);
    List<Activity> findByCategory(String category);  
    // Gabri: cambiato da findByOwner_Id (Optional) a findFirstByOwner_Id
    // perché durante i test sono stati creati più record per lo stesso owner.
    // findFirst evita NonUniqueResultException. In produzione dovrebbe esserci
    // un vincolo UNIQUE su owner_id, ma per ora gestiamo lato query.
    Optional<Activity> findFirstByOwner_Id(Integer ownerId);
}
