package com.generation.bookly.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.mapper.UserMapper;
import com.generation.bookly.model.User;
import com.generation.bookly.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService 
{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


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
        
        // --- AGGIUNGI QUESTA RIGA ---
        // Criptiamo la password prima di salvare l'entità
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // ----------------------------

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
        //existingUser.setPassword(userDTO.getPassword());
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

        // 1. Controllo di sicurezza: la nuova password è uguale alla vecchia?
        // Nota: Con l'hash non puoi usare .equals(), devi usare .matches()
        if (passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            throw new IllegalArgumentException("La nuova password è identica a quella attuale");
        }

        // 2. HASHING della nuova password prima del salvataggio
        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        // 3. Salvataggio della password aggiornata
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

    // Mappa temporanea: ID Utente -> Codice OTP
    private final Map<Integer, String> otpStorage = new HashMap<>();

    public void generateAndSendOtp(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        
        // Genera OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(userId, otp);
        
        // USA IL TUO SERVICE ESISTENTE
        emailService.inviaMailOTP(user.getEmail(), user.getFirstName(), otp);
        
        // Mantieni il log per sicurezza durante i test
        System.out.println("DEBUG OTP per utente " + userId + ": " + otp);
    }

    public UserDTO verifyAndChangePassword(int id, String otp, String newPassword) {
        String validOtp = otpStorage.get(id);
        
        if (validOtp == null || !validOtp.equals(otp)) {
            throw new IllegalArgumentException("Codice OTP errato o scaduto");
        }
        
        // Se l'OTP è corretto, procediamo con il cambio password (hashato!)
        UserDTO updated = updatePassword(id, newPassword);
        otpStorage.remove(id); // Puliamo la mappa
        return updated;
    }
}