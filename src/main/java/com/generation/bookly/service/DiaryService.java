package com.generation.bookly.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.mapper.DiaryMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Diary;
import com.generation.bookly.model.Diary.Available;
import com.generation.bookly.repository.ActivityRepository;
import com.generation.bookly.repository.DiaryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService 
{

    private final DiaryRepository diaryRepository;
    private final DiaryMapper diaryMapper;
    private final ActivityRepository activityRepository;

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
        diary = diaryRepository.save(diary);
        return diaryMapper.toDTO(diary);
    }

    // Recupera slot disponibili per una certa attività e data
    public List<DiaryDTO> getAvailableSlots(Integer activityId, LocalDate date)
    {
        return diaryRepository.findByActivityIdAndDateAndAvailable(activityId, date, Diary.Available.AVAILABLE)
                .stream()
                .map(diaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Gabri: aggiunto per il modale del titolare — restituisce tutti gli slot del giorno
    // inclusi NOTAVAILABLE (chiusure) e BOOKED, così il titolare può vederli ed eliminarli.
    public List<DiaryDTO> getAllSlots(Integer activityId, LocalDate date)
    {
        return diaryRepository.findByActivityIdAndDate(activityId, date)
                .stream()
                .map(diaryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryDTO updateSlot(int id, DiaryDTO dto) 
    {

        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Slot non trovato"));

        if(diary.getAvailable() != Available.AVAILABLE) 
        {
            throw new IllegalStateException("Slot non modificabile");
        }

        // PATCH: aggiorna solo se presente
        if(dto.getDate() != null) 
        {
            diary.setDate(dto.getDate());
        }

        if(dto.getStartHour() != null) 
        {
            diary.setStartHour(dto.getStartHour());
        }

        if(dto.getEndHour() != null) 
        {
            diary.setEndHour(dto.getEndHour());
        }

        if(dto.getAvailable() != null)
        {
            diary.setAvailable(Available.valueOf(dto.getAvailable()));
        }

        return diaryMapper.toDTO(diaryRepository.save(diary));
    }

    @Transactional
    public void deleteSlot(int id) 
    {
        diaryRepository.deleteById(id);
    
    }

   @Transactional
    public void generateSlotsForActivity(Integer activityId, int daysAhead, int slotDurationMinutes) {
        // 1. Recupero l'attività (serve per il legame @ManyToOne)
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Attività non trovata"));

        // 2. Logica di generazione (quella definita precedentemente)
        LocalTime openingTime = LocalTime.of(9, 0); 
        LocalTime closingTime = LocalTime.of(18, 0);
        LocalDate startDate = LocalDate.now();

        for (int i = 0; i < daysAhead; i++) {
            LocalDate currentDate = startDate.plusDays(i);

            // Evita duplicati per lo stesso giorno
            if (diaryRepository.existsByActivityAndDate(activity, currentDate)) {
                continue; 
            }

            LocalTime timeTracker = openingTime;
            while (timeTracker.plusMinutes(slotDurationMinutes).isBefore(closingTime) || 
                   timeTracker.plusMinutes(slotDurationMinutes).equals(closingTime)) {
                
                Diary slot = new Diary();
                slot.setActivity(activity);
                slot.setDate(currentDate);
                slot.setStartHour(timeTracker);
                slot.setEndHour(timeTracker.plusMinutes(slotDurationMinutes));
                slot.setAvailable(Available.AVAILABLE); // O il tuo valore Enum
                
                diaryRepository.save(slot);
                timeTracker = timeTracker.plusMinutes(slotDurationMinutes);
            }
        }
    }
}