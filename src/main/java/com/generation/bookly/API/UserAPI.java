package com.generation.bookly.API;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookly/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserAPI 
{

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO saved = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore validazione dati");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore aggiornamento utente");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint per l'admin per vedere quanti report ha un utente
    @PatchMapping("/{id}/increment-reports")
    public ResponseEntity<UserDTO> reportUser(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.incrementReports(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<?> updateEmail(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String newEmail = request.get("email");
            if (newEmail == null || newEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email non valida");
            }
            return ResponseEntity.ok(userService.updateEmail(id, newEmail));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("password");
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password non valida");
            }
            return ResponseEntity.ok(userService.updatePassword(id, newPassword));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}