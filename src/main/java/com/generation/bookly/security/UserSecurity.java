package com.generation.bookly.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
    
    public boolean isSelf(int userId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        // authentication.getName() = email dal token (subject)
        // implementa lookup su DB se necessario per confrontare id/email
        return false; // sostituisci con logica reale
    }
}
