package com.generation.bookly.API;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.service.ReportingService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookly/api/reports")
@RequiredArgsConstructor
public class ReportingAPI {

    private final ReportingService reportingService;

    @PostMapping
    public ResponseEntity<ReportingDTO> submitReport(@RequestBody ReportingDTO dto) {
        return ResponseEntity.ok(reportingService.createReport(dto));
    }

    // Endpoint per l'admin per risolvere/chiudere una segnalazione
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable int id, @RequestParam String status) {
        // status può essere APPROVED o REJECTED
        reportingService.resolveReport(id, status);
        return ResponseEntity.noContent().build();
    }

    // Ottieni tutte le segnalazioni (utile per la dashboard Admin)
    @GetMapping
    public ResponseEntity<List<ReportingDTO>> getAll(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(reportingService.findAllByStatus(status));
        }
        return ResponseEntity.ok(reportingService.findAll());
    }

    // Ottieni una singola segnalazione per ID
    @GetMapping("/{id}")
    public ResponseEntity<ReportingDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(reportingService.findById(id));
    }

    // Elimina una segnalazione (es. se inserita per errore o obsoleta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        reportingService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}