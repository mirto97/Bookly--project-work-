package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
