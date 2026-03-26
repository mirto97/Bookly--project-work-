package com.generation.bookly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.mapper.ActivityMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Booking;
import com.generation.bookly.repository.ActivityRepository;
import com.generation.bookly.repository.BookingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService 
{

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public List<ActivityDTO> getAllApprovedActivities() 
    {
        return activityRepository.findByStatus(Activity.ActivityStatus.APPROVED)
                .stream()
                .map(activityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActivityDTO registerActivity(ActivityDTO dto)
    {
        Activity activity = activityMapper.toEntity(dto);
        activity.setId(null);
        activity.setStatus(Activity.ActivityStatus.PENDING); // In attesa di approvazione admin
        return activityMapper.toDTO(activityRepository.save(activity));
    }

    public List<ActivityDTO> getAllActivityPending() 
    {
         return activityRepository.findByStatus(Activity.ActivityStatus.PENDING)
                .stream()
                .map(activityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Activity updateStatus(int id, Activity.ActivityStatus newStatus) 
    {
        // 1. Cerchiamo l'attività
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity non trovata"));

        // 2. Aggiorniamo solo lo status
        activity.setStatus(newStatus);

        // 3. Salviamo (JPA farà un UPDATE mirato)
        return activityRepository.save(activity);
    }

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAppointmentsByActivityId(int activityId) 
    {
        // Verifichiamo prima se l'attività esiste
        if (!activityRepository.existsById(activityId)) 
        {
            throw new EntityNotFoundException("Activity con ID " + activityId + " non trovata");
        }
        
        // Cerchiamo tutti gli appuntamenti legati a questa attività
        return bookingRepository.findByServiceActivityId(activityId);
    }

    public void deleteActivityById(int id) 
    {
        // Verifichiamo prima se l'attività esiste
        if(!activityRepository.existsById(id)) 
        {
            throw new EntityNotFoundException("Activity con ID " + id + " non trovata");
        }

        activityRepository.deleteById(id);
    }
        
}