package com.generation.bookly.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.bookly.model.Activity;
import com.generation.bookly.model.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Integer> 
{
    // Trova gli slot liberi per un'attività in una certa data
    List<Diary> findByActivityIdAndDateAndAvailable(Integer activityId, LocalDate date, Diary.Available available);

    // Gabri: aggiunto per il calendario del titolare — restituisce tutti gli slot di un giorno
    // indipendentemente dallo stato (AVAILABLE, NOTAVAILABLE, BOOKED), così il modale
    // può mostrare anche le chiusure giornaliere e permettere di eliminarle.
    List<Diary> findByActivityIdAndDate(Integer activityId, LocalDate date);

    boolean existsByActivityAndDate(Activity activity, LocalDate date);
    
    // Opzionale: utile per cancellare slot vecchi
    void deleteByActivityAndDate(Activity activity, LocalDate date);
}
