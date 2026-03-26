package com.generation.bookly.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.mapper.ReportingMapper;
import com.generation.bookly.model.Reporting;
import com.generation.bookly.repository.ReportingRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final ReportingMapper reportingMapper;

    // CREATE
    public ReportingDTO createReport(ReportingDTO dto) {
        Reporting reporting = reportingMapper.toEntity(dto);
        reporting.setStatus(Reporting.ReportingStatus.PENDING);
        reporting.setCreatedAt(LocalDateTime.now());
        return reportingMapper.toDTO(reportingRepository.save(reporting));
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
}