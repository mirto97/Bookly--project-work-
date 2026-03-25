package com.generation.bookly.service;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.mapper.UserMapper;
import com.generation.bookly.model.User;
import com.generation.bookly.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO incrementReports(int userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        
        user.setReportingsReceived(user.getReportingsReceived() + 1);
        
        // Logica ban automatico se superano i 5 report
        if (user.getReportingsReceived() >= 5) {
            // Qui potresti disabilitare l'account o loggare l'evento
        }
        
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));
        return userMapper.toDTO(user);
    }
}