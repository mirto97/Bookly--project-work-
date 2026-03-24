package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

}
