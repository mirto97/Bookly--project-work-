package com.generation.bookly.API;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.service.ReportingService;
import com.generation.bookly.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ReportingService reportingService;


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

    // Endpoint PATCH per aggiornare solo la bio dell'utente.
    // Usiamo Map<String, String> invece di un DTO dedicato perché il body è un singolo campo:
    // { "bio": "..." } — coerente con il pattern già usato per email e password.
    @PatchMapping("/{id}/bio")
    public ResponseEntity<?> updateBio(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String bio = request.get("bio");
            if (bio == null) {
                return ResponseEntity.badRequest().body("Bio non valida");
            }
            return ResponseEntity.ok(userService.updateBio(id, bio));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint PATCH per aggiornare solo la foto profilo dell'utente (via URL).
    @PatchMapping("/{id}/photo")
    public ResponseEntity<?> updatePhoto(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String photoUrl = request.get("photoUrl");
            if (photoUrl == null || photoUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("URL foto non valido");
            }
            return ResponseEntity.ok(userService.updatePhoto(id, photoUrl));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Status non valido");
            }
            return ResponseEntity.ok(userService.updateStatus(id, status));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Corrisponde a: this.http.post(`${this.apiUrl}/${userId}/send-otp`, {})
    @PostMapping("/{id}/send-otp")
    public ResponseEntity<?> sendOtp(@PathVariable int id) {
        try {
            userService.generateAndSendOtp(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Corrisponde a: this.http.patch(`${this.apiUrl}/${id}/password-with-otp`, { otp, password })
    @PatchMapping("/{id}/password-with-otp")
    public ResponseEntity<?> verifyOtpAndChangePassword(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String otp = request.get("otp");
            String newPassword = request.get("password");
            
            if (otp == null || newPassword == null) {
                return ResponseEntity.badRequest().body("Dati mancanti (otp o password)");
            }
            
            UserDTO updated = userService.verifyAndChangePassword(id, otp, newPassword);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserDTO userDTO) {
        try {
            UserDTO saved = userService.createAdmin(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore validazione dati");
        }
    }

    @GetMapping("/banned")
    public ResponseEntity<List<UserDTO>> getBannedUsers() {
        // Dobbiamo aggiungere questo metodo nel service
        List<UserDTO> bannedUsers = userService.getUsersByStatus("BANNED");
        return ResponseEntity.ok(bannedUsers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<UserDTO> reactivateUser(@PathVariable int id) {
        // Usiamo il metodo che abbiamo scritto prima
        UserDTO updatedUser = userService.reactivateUser(id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/reportings")
    public ResponseEntity<List<ReportingDTO>> getUserReportings(@PathVariable int id) {
        // Dobbiamo recuperare tutti i report dove reported_user_id = id
        List<ReportingDTO> reports = reportingService.getReportsByReportedUserId(id);
        return ResponseEntity.ok(reports);
    }
}