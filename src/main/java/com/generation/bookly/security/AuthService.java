package com.generation.bookly.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.bookly.model.User;
import com.generation.bookly.model.User.Role;
import com.generation.bookly.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private static final Set<Role> PUBLIC_REGISTER_ALLOWED_ROLES = Set.of(Role.GUEST, Role.OWNER);

    public String register(RegisterRequestDTO request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email già esistente");
        });

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Password HASHATA
        user.setRole(parsePublicRole(request.getRole()));
        
        System.out.println("RUOLO DA SALVARE: " + user.getRole());
        userRepository.save(user);
        
        return jwtService.generateToken(user);
    }

    private Role parsePublicRole(String rawRole) {
        if (rawRole == null || rawRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Role obbligatorio. Valori ammessi: [GUEST, OWNER]");
        }

        Role parsedRole;
        try {
            parsedRole = Role.valueOf(rawRole.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Role non valido: " + rawRole);
        }

        if (!PUBLIC_REGISTER_ALLOWED_ROLES.contains(parsedRole)) {
            throw new IllegalArgumentException("Role non consentito. Valori ammessi: [GUEST, OWNER]");
        }

        return parsedRole;
    }

    public String authenticate(LoginRequestDTO request) {
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    if (!user.isEnabled()) {
        throw new DisabledException("Account disabled");
    }
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Bad credentials");
    }
    return jwtService.generateToken(user);
    }
}
