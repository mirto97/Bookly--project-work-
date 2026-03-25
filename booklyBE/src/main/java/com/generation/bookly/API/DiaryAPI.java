package com.generation.bookly.API;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryAPI {

    private final DiaryService diaryService;

    @GetMapping("/available/{activityId}")
    public List<DiaryDTO> getAvailable(
            @PathVariable Integer activityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.getAvailableSlots(activityId, date);
    }
}