package com.generation.bookly.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.mapper.BookingMapper;
import com.generation.bookly.model.Booking;
import com.generation.bookly.model.Diary;
import com.generation.bookly.repository.BookingRepository;
import com.generation.bookly.repository.DiaryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//gabri
@Service
@RequiredArgsConstructor
public class BookingService 
{

    private final BookingRepository bookingRepository;
    private final DiaryRepository diaryRepository;
    private final BookingMapper bookingMapper; // Il tuo mapper MapStruct
    private final EmailService emailService; // Il servizio per inviare email

   @Transactional
    public BookingDTO createBooking(BookingDTO dto) {
        // 1. Recupero lo slot (qui abbiamo già data e ora!)
        Diary diary = diaryRepository.findById(dto.getDiaryId())
            .orElseThrow(() -> new EntityNotFoundException("Slot non trovato"));

        // 2. Controllo disponibilità
        if(diary.getAvailable() != Diary.Available.AVAILABLE) {
            throw new IllegalStateException("Lo slot selezionato non è più disponibile");
        }

        // 3. Mappatura e salvataggio
        Booking booking = bookingMapper.toEntity(dto);
        booking.setStatus(Booking.BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);

        // 4. Aggiorno il diario
        diary.setAvailable(Diary.Available.BOOKED);
        diaryRepository.save(diary);

        // --- 5. LOGICA INVIO EMAIL ---
        try {
            // Costruiamo i dettagli usando i dati dell'oggetto 'diary' appena recuperato
            String orario = diary.getStartHour().toString().substring(0, 5); // Toglie i secondi (es. 09:00)
            String dettagli = "Appuntamento il " + diary.getDate() + " alle ore " + orario;

            // Invio asincrono
            emailService.inviaMailConferma(
                dto.getEmailCliente(),    // Assicurati che il tuo BookingDTO abbia il campo email
                dto.getNomeCliente(),     // Assicurati che abbia il campo nome
                dettagli
            );
        } catch (Exception e) {
            // Logghiamo l'errore ma non blocchiamo la transazione della prenotazione
            System.err.println("Impossibile inviare la mail: " + e.getMessage());
        }
        // ------------------------------

        return bookingMapper.toDTO(saved);
    }

    public BookingDTO updateBookingById(int id, BookingDTO dto) {
        // Verifichiamo che la prenotazione esista prima di aggiornarla
        bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        // ERRORE PRECEDENTE: si chiamava booking.setUserId() e booking.setDiaryId()
        // ma Booking NON ha campi "userId" o "diaryId" diretti!
        // Ha relazioni @ManyToOne: "private User user" e "private Diary diary".
        // Quindi i setter corretti sarebbero setUser(User) e setDiary(Diary),
        // che richiederebbero di caricare le entità dal DB.
        //
        // SOLUZIONE PIÙ PULITA: usiamo il mapper per ricostruire l'entity dal DTO.
        // Il mapper sa già come trasformare userId → user.id, diaryId → diary.id, ecc.
        // Impostiamo l'ID manualmente per fare un UPDATE invece di un INSERT.
        Booking booking = bookingMapper.toEntity(dto);
        booking.setId(id); // senza setId(), JPA farebbe un INSERT invece di UPDATE

        Booking updated = bookingRepository.save(booking);
        return bookingMapper.toDTO(updated);
    }

    // Metodo delete: verifica che esista, poi elimina
    // deleteById() di JPA non lancia eccezione se l'ID non esiste,
    // per questo controlliamo prima con findById().
    public void deleteBookingById(int id) {
        bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
        bookingRepository.deleteById(id);
    }

    // Metodo PATCH: aggiorna solo lo status senza toccare gli altri campi.
    // È una operazione di aggiornamento parziale (PATCH), diversa dall'update completo (PUT).
    // Recuperiamo l'entity, modifichiamo solo il campo status, salviamo.
    public BookingDTO updateBookingStatusById(int id, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        booking.setStatus(status); // solo questo campo viene modificato
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    public List<BookingDTO> getByActivityId(int activityId) {
    return bookingRepository.findByService_Activity_Id(activityId)
        .stream()
        .map(bookingMapper::toDTO)
        .collect(Collectors.toList());
    }
}