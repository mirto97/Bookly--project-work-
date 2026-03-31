package com.generation.bookly.service;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.mapper.UserMapper;
import com.generation.bookly.model.User;
import com.generation.bookly.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService 
{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO incrementReports(int userId) 
    {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        
        user.setReportingsReceived(user.getReportingsReceived() + 1);
        
        // Logica ban automatico se superano i 5 report
        if(user.getReportingsReceived() >= 5) 
        {
            // Qui potresti disabilitare l'account o loggare l'evento
        }
        
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO getUserById(int id) 
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));
        return userMapper.toDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utente con email " + email + " non trovato"));
        return userMapper.toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.toDTO(userRepository.findAll());
    }

    public UserDTO createUser(UserDTO userDTO) {
        // Verifica esistenza email
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email già esistente");
        });

        User user = userMapper.toEntity(userDTO);
        // Impostazioni default
        user.setReportingsReceived(0);
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    public UserDTO updateUser(int id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));

        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setDob(userDTO.getDob());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setRole(User.Role.valueOf(userDTO.getRole()));
        existingUser.setReportingsReceived(userDTO.getReportingsReceived());

        User saved = userRepository.save(existingUser);
        return userMapper.toDTO(saved);
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente con id " + id + " non trovato");
        }
        userRepository.deleteById(id);
    }

    public UserDTO updateEmail(int id, String newEmail) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));

        // Controllo se la nuova email è diversa dalla vecchia
        if (newEmail.equals(existingUser.getEmail())) {
            throw new IllegalArgumentException("La nuova email è identica a quella attuale");
        }

        // Verifica se email già usata da altro utente
        userRepository.findByEmail(newEmail).ifPresent(u -> {
            if (u.getId() != existingUser.getId()) {
                throw new IllegalArgumentException("Email già esistente");
            }
        });

        existingUser.setEmail(newEmail);
        User saved = userRepository.save(existingUser);
        return userMapper.toDTO(saved);
    }

    public UserDTO updatePassword(int id, String newPassword) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));

        if (newPassword.equals(existingUser.getPassword())) {
            throw new IllegalArgumentException("La nuova password è identica a quella attuale");
        }

        existingUser.setPassword(newPassword);
        User saved = userRepository.save(existingUser);
        return userMapper.toDTO(saved);
    }

    // Aggiornamento parziale (PATCH) della bio: aggiorna solo questo campo senza toccare il resto.
    // Usato dalla pagina profilo del guest, dove la bio è modificabile inline.
    public UserDTO updateBio(int id, String bio) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));
        user.setBio(bio);
        return userMapper.toDTO(userRepository.save(user));
    }

    // Aggiornamento parziale (PATCH) della foto profilo via URL.
    // Salviamo solo l'URL dell'immagine, non il binario — l'immagine è hostata esternamente.
    public UserDTO updatePhoto(int id, String photoUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));
        user.setPhotoUrl(photoUrl);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateStatus(int id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente con id " + id + " non trovato"));
        user.setStatus(User.Status.valueOf(status.toUpperCase()));
        return userMapper.toDTO(userRepository.save(user));
    }
}