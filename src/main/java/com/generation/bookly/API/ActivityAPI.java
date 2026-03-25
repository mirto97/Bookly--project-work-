package com.generation.bookly.API;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityAPI {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllApproved() {
        return ResponseEntity.ok(activityService.getAllApprovedActivities());
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> register(@RequestBody ActivityDTO dto) {
        return ResponseEntity.ok(activityService.registerActivity(dto));
    }
}