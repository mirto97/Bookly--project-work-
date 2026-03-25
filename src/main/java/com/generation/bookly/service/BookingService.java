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

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final DiaryRepository diaryRepository;
    private final BookingMapper bookingMapper; // Il tuo mapper MapStruct

    @Transactional
    public BookingDTO createBooking(BookingDTO dto) {
        Diary diary = diaryRepository.findById(dto.getDiaryId())
            .orElseThrow(() -> new EntityNotFoundException("Slot non trovato"));

        // 2. Controllo disponibilità
        if (diary.getAvailable() != Diary.Available.AVAILABLE) {
            throw new IllegalStateException("Lo slot selezionato non è più disponibile");
        }

        // 3. Mappatura e salvataggio
        Booking booking = bookingMapper.toEntity(dto);
        booking.setStatus(Booking.BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);

        // 4. Aggiorno il diario (Logica di business)
        diary.setAvailable(Diary.Available.BOOKED);
        diaryRepository.save(diary);

        return bookingMapper.toDTO(saved);
    }

    public List<BookingDTO> getUserBookings(int userId) {
        // Recuperiamo le entità
        List<Booking> entities = bookingRepository.findByUserId(userId);
        
        // Trasformiamole in DTO (MapStruct gestisce la conversione della lista)
        return entities.stream()
                       .map(bookingMapper::toDTO)
                       .collect(Collectors.toList());
    }
}