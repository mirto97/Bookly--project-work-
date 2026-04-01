package com.generation.bookly.API;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.bookly.repository.AuthenticationResponse;
import com.generation.bookly.security.AuthService;
import com.generation.bookly.security.LoginRequestDTO;
import com.generation.bookly.security.RegisterRequestDTO;



@RestController
// Gabri: Ho corretto il path da /api/auth a /bookly/api/auth per allinearlo
// agli altri controller e al frontend, che chiamava /bookly/api/auth/login.
// Prima il login dava sempre 404.
@RequestMapping("/bookly/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequestDTO request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequestDTO request) {
        try {
            String token = authService.authenticate(request);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (DisabledException e) {
            // Questo scatta se isEnabled() nell'Entity User restituisce false
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Il tuo account è temporaneamente sospeso. Un amministratore sta revisionando le segnalazioni ricevute."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Email o password errati."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Errore durante l'autenticazione."));
        }
    }

}