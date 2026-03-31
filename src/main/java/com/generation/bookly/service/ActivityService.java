package com.generation.bookly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.mapper.ActivityMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Booking;
import com.generation.bookly.model.User;
import com.generation.bookly.repository.ActivityRepository;
import com.generation.bookly.repository.BookingRepository;
import com.generation.bookly.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService 
{

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    // Gabri: Ho aggiunto UserRepository perché registerActivity deve caricare l'oggetto User
    // completo dal DB prima di salvare l'Activity (JPA richiede l'entity, non solo l'id).
    private final UserRepository userRepository;

    public List<Activity> findAll() 
    {
        return activityRepository.findAll();
    }

    public ActivityDTO getActivityById(int id) 
    {
        Activity activity = activityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Activity non trovata con id: " + id));
        return activityMapper.toDTO(activity);
    }
    
    public ActivityDTO getActivityByOwnerId(int ownerId) {
        Activity activity = (activityRepository.findFirstByOwner_Id(ownerId))
            .orElseThrow(() -> new EntityNotFoundException("Nessuna attività per owner id: " + ownerId));
        return activityMapper.toDTO(activity);
    }

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
        // Gabri: lo status viene sempre forzato a PENDING lato backend,
        // anche se il frontend invia un valore diverso, per sicurezza.
        activity.setStatus(Activity.ActivityStatus.PENDING);
        // Gabri: registrationDate viene impostata lato backend al momento della registrazione,
        // così il frontend non deve inviarla e la colonna NOT NULL viene sempre soddisfatta.
        activity.setRegistrationDate(java.time.LocalDateTime.now());

        // Gabri: Ho aggiunto questo blocco perché il mapper ignora il campo owner (vedi ActivityMapper).
        // Carichiamo qui l'oggetto User completo dal DB usando l'ownerId arrivato dal DTO,
        // e lo settiamo sull'entity prima del save. Senza questo, Hibernate lanciava un 500
        // perché activity.owner era null ma la colonna owner_id è NOT NULL.
        User owner = userRepository.findById(dto.getOwnerId())
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + dto.getOwnerId()));
        activity.setOwner(owner);

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

    private final BookingRepository bookingRepository;

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