package com.generation.bookly.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Integer> 
{
    // Trova gli slot liberi per un'attività in una certa data
    List<Diary> findByActivityIdAndDateAndAvailable(Integer activityId, LocalDate date, Diary.Available available);
}
