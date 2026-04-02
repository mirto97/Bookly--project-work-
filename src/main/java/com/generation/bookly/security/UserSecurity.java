package com.generation.bookly.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.generation.bookly.repository.UserRepository;

@Component("userSecurity")
public class UserSecurity {
    @Autowired
    private UserRepository userRepository;
    
    public boolean isSelf(Integer userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        
        // Recupera l'utente dal DB tramite l'email del token
        return userRepository.findByEmail(currentUserEmail)
                .map(user -> user.getId() == userId) // Confronta gli ID
                .orElse(false);
    }
}
