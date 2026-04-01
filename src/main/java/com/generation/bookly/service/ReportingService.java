package com.generation.bookly.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.mapper.ReportingMapper;
import com.generation.bookly.model.Reporting;
import com.generation.bookly.repository.ReportingRepository;
import com.generation.bookly.repository.UserRepository;
import com.generation.bookly.model.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final ReportingMapper reportingMapper;
    private final UserRepository userRepository;


    // CREATE
    public ReportingDTO createReport(ReportingDTO dto) {
        System.out.println("DEBUG REPORT -> BookingID: " + dto.getBookingId() + " | AuthorID: " + dto.getAuthorId());
        
        boolean exists = reportingRepository.existsByBookingIdAndAuthorId(dto.getBookingId(), dto.getAuthorId());
        System.out.println("DEBUG REPORT -> Esiste già? " + exists);

        if (exists) {
            throw new RuntimeException("GIA_SEGNALATO");
        }
        // 1. Controllo anti-spam/duplicati
        if (reportingRepository.existsByBookingIdAndAuthorId(dto.getBookingId(), dto.getAuthorId())) {
            throw new RuntimeException("Hai già inviato una segnalazione per questa prenotazione.");
        }

        // 2. Mappatura e salvataggio report
        Reporting reporting = reportingMapper.toEntity(dto);
        reporting.setStatus(Reporting.ReportingStatus.PENDING);
        reporting.setCreatedAt(LocalDateTime.now());
        Reporting saved = reportingRepository.save(reporting);

        // 3. Incremento contatore e Ban (solo se il report è di tipo CLIENT)
        if (dto.getReportedUserId() != null) {
            userRepository.findById(dto.getReportedUserId()).ifPresent(guest -> {
                int currentCount = guest.getReportingsReceived() + 1;
                guest.setReportingsReceived(currentCount);
                
                if (currentCount >= 5) {
                    guest.setStatus(User.Status.BANNED);
                }
                userRepository.save(guest);
            });
        }

        return reportingMapper.toDTO(saved);
    }

    // READ ALL
    public List<ReportingDTO> findAll() {
        return reportingRepository.findAll().stream()
                .map(reportingMapper::toDTO)
                .toList();
    }

    // READ BY STATUS
    public List<ReportingDTO> findAllByStatus(String status) {
        Reporting.ReportingStatus s = Reporting.ReportingStatus.valueOf(status.toUpperCase());
        return reportingRepository.findByStatus(s).stream()
                .map(reportingMapper::toDTO)
                .toList();
    }

    // READ BY ID
    public ReportingDTO findById(int id) {
        Reporting report = reportingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Segnalazione non trovata con ID: " + id));
        return reportingMapper.toDTO(report);
    }

    // UPDATE (Resolve)
    public void resolveReport(int id, String status) {
        Reporting report = reportingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Segnalazione non trovata"));
        
        Reporting.ReportingStatus newStatus = Reporting.ReportingStatus.valueOf(status.toUpperCase());
        report.setStatus(newStatus);
        
        // Logica extra: se approvata, potresti voler triggerare azioni sull'entità segnalata
        // (es. bannare l'utente o nascondere la recensione)
    }

    // DELETE
    public void deleteReport(int id) {
        if (!reportingRepository.existsById(id)) {
            throw new EntityNotFoundException("Impossibile eliminare: segnalazione non trovata");
        }
        reportingRepository.deleteById(id);
    }

    public List<ReportingDTO> getReportsByReportedUserId(int userId) {
        // Assicurati che il ReportingRepository abbia questo metodo
        return reportingMapper.toDTO(reportingRepository.findByReportedUser_Id(userId));
    }
}