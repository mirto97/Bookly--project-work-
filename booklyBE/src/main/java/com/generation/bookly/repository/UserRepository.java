package com.generation.bookly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
