package com.generation.bookly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.User;

public interface UserRepository extends JpaRepository<User, Integer>
{
    Optional<User> findByEmail(String email);
    // Per il filtro per status (attivo, bannato, etc.)
    List<User> findByStatus(User.Status status);
}
