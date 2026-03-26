package com.generation.bookly.API;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookly/api/users")
@RequiredArgsConstructor
public class UserAPI 
{

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable int id) 
    {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Endpoint per l'admin per vedere quanti report ha un utente
    @PatchMapping("/{id}/increment-reports")
    public ResponseEntity<UserDTO> reportUser(@PathVariable int id) 
    {
        return ResponseEntity.ok(userService.incrementReports(id));
    }
}