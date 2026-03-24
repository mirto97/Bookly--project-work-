package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Diary;

public interface CalendarRepository extends JpaRepository<Diary, Integer> {

}
