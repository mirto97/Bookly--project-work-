package com.generation.bookly.API;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.mapper.ActivityMapper;
import com.generation.bookly.mapper.BookingMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Booking;
import com.generation.bookly.service.ActivityService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookly/api/activities")
@RequiredArgsConstructor
public class ActivityAPI {

    private final ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @GetMapping("/approved")
    public ResponseEntity<List<ActivityDTO>> findAllActivityApproved() {
        return ResponseEntity.ok(activityService.getAllApprovedActivities());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ActivityDTO>> findAllActivityPending() {
        return ResponseEntity.ok(activityService.getAllActivityPending());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ActivityDTO> updateStatus(
            @PathVariable int id, 
            @RequestParam Activity.ActivityStatus status) {
        
        try {
            Activity updated = activityService.updateStatus(id, status);
            return ResponseEntity.ok(activityMapper.toDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}