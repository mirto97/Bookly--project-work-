package com.generation.bookly.API;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.mapper.ActivityMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.service.ActivityService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookly/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ActivityAPI 
{

    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody ActivityDTO dto) 
    {
        try 
        {
            dto = activityService.registerActivity(dto);
            return ResponseEntity.status(201).body(dto);
        } 
        catch (ConstraintViolationException e) 
        {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ActivityDTO>> findAllActivityApproved() 
    {
        return ResponseEntity.ok(activityService.getAllApprovedActivities());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ActivityDTO>> findAllActivityPending() 
    {
        return ResponseEntity.ok(activityService.getAllActivityPending());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ActivityDTO> updateStatus
    (
        @PathVariable int id, 
        @RequestParam Activity.ActivityStatus status
    ) 
    {
        try 
        {
            Activity updated = activityService.updateStatus(id, status);
            return ResponseEntity.ok(activityMapper.toDTO(updated));
        } 
        catch (EntityNotFoundException e) 
        {
            return ResponseEntity.notFound().build();
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActivityDTO> deleteActivity(@PathVariable int id) 
    {
        activityService.deleteActivityById(id);
        return ResponseEntity.noContent().build();
    } 
}