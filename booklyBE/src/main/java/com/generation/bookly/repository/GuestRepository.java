package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Integer>{

}
