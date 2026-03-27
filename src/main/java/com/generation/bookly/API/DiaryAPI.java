package com.generation.bookly.API;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.service.DiaryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookly/api/diary")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DiaryAPI 
{

    private final DiaryService diaryService;

    // Gabri: endpoint per il titolare — restituisce tutti gli slot di un giorno
    // (AVAILABLE, NOTAVAILABLE, BOOKED) così il modale del calendario può mostrare
    // anche le chiusure e permettere di eliminarle.
    @GetMapping("/all/{activityId}")
    public List<DiaryDTO> getAllSlots
    (
        @PathVariable Integer activityId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    )
    {
        return diaryService.getAllSlots(activityId, date);
    }

    @GetMapping("/available/{activityId}")
    public List<DiaryDTO> getAvailable
    (
        @PathVariable Integer activityId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) 
    {
        return diaryService.getAvailableSlots(activityId, date);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody DiaryDTO dto) 
    {
        try 
        {
            dto = diaryService.createSlot(dto);
            return ResponseEntity.status(201).body(dto);
        } 
        catch (ConstraintViolationException e) 
        {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DiaryDTO> updateSlot
    (
        @PathVariable int id,
        @RequestBody DiaryDTO dto
    ) 
    {
        try 
        {
            return ResponseEntity.ok(diaryService.updateSlot(id, dto));
        } 
        catch(EntityNotFoundException e) 
        {
            return ResponseEntity.notFound().build();
        } 
        catch(Exception e) 
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DiaryDTO> deleteSlot(@PathVariable int id) 
    {
        diaryService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }


}