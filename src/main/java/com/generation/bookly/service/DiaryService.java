package com.generation.bookly.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.mapper.DiaryMapper;
import com.generation.bookly.model.Diary;
import com.generation.bookly.repository.DiaryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService 
{

    private final DiaryRepository diaryRepository;
    private final DiaryMapper diaryMapper;

    // Crea nuovi slot di disponibilità
    @Transactional
    public DiaryDTO createSlot(DiaryDTO dto) 
    {
        Diary diary = diaryMapper.toEntity(dto);
        // Default a AVAILABLE se non specificato
        if(diary.getAvailable() == null) 
        {
            diary.setAvailable(Diary.Available.AVAILABLE);
        }
        return diaryMapper.toDTO(diaryRepository.save(diary));
    }

    // Recupera slot disponibili per una certa attività e data
    public List<DiaryDTO> getAvailableSlots(Integer activityId, LocalDate date) 
    {
        return diaryRepository.findByActivityIdAndDateAndAvailable(activityId, date, Diary.Available.AVAILABLE)
                .stream()
                .map(diaryMapper::toDTO)
                .collect(Collectors.toList());
    }
}