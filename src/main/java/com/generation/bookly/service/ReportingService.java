package com.generation.bookly.service;

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
public class ReportingService 
{

    private final ReportingRepository reportingRepository;
    private final ReportingMapper reportingMapper;
    private final UserService userService; // Iniettiamo il service utente

    @Transactional
    public ReportingDTO createReport(ReportingDTO dto) 
    {
        Reporting report = reportingMapper.toEntity(dto);
        report.setStatus(Reporting.ReportingStatus.PENDING);
        
        Reporting saved = reportingRepository.save(report);

        // Se la segnalazione è contro un utente (tipo CLIENT), 
        // potremmo già incrementare il counter o farlo solo dopo l'approvazione admin
        return reportingMapper.toDTO(saved);
    }

    @Transactional
    public void resolveReport(int reportId, Reporting.ReportingStatus status) 
    {
        Reporting report = reportingRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Segnalazione non trovata"));
        
        report.setStatus(status);
        
        // Se l'admin approva il report, incrementiamo i report ricevuti dall'autore della violazione
        if(status == Reporting.ReportingStatus.APPROVED && report.getType() == Reporting.ReportingType.CLIENT) 
        {
             // Nota: qui servirebbe l'ID dell'utente segnalato (il target)
             // userService.incrementReports(targetUserId);
        }
        
        reportingRepository.save(report);
    }

    @Transactional
    public void resolveReport(int reportId, String status) 
    {
        // 1. Trova la segnalazione
        Reporting report = reportingRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Segnalazione non trovata"));

        // 2. Converte la stringa in Enum (es. "APPROVED" -> ReportingStatus.APPROVED)
        try 
        {
            Reporting.ReportingStatus newStatus = Reporting.ReportingStatus.valueOf(status.toUpperCase());
            report.setStatus(newStatus);
            
            // 3. Se approvato e riguarda un utente, potresti agire qui
            // (es. bloccare l'utente o mandare una mail)
            
            reportingRepository.save(report);
        } 
        catch (IllegalArgumentException e) 
        {
            throw new RuntimeException("Status non valido: " + status);
        }
    }
}