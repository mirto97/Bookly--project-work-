package com.generation.bookly.API;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.service.ReportingService;
import lombok.RequiredArgsConstructor;
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

    
}