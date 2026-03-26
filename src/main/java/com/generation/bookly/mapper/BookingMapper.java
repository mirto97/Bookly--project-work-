package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.model.Booking;

// "uses" dice a MapStruct quali altri mapper può usare per convertire tipi annidati.
// Ad esempio UserMapper sa come mappare User ↔ UserDTO, ServiceMapper sa per Service ↔ ServiceDTO.
// ActivityMapper è incluso perché ServiceMapper lo usa internamente (Service ha un campo Activity).

// RIMOSSO "uses" che causavano dipendenze circolari. 
// Poiché mappiamo manualmente gli ID, MapStruct non ha bisogno degli altri mapper qui.
@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Da DTO → Entity (es. quando riceviamo una richiesta POST/PUT dal client)
    //
    // Il problema: nel DTO abbiamo campi "piatti" come userId (int),
    // ma nell'entity Booking abbiamo relazioni @ManyToOne come "private User user".
    // MapStruct non sa da solo che "userId" del DTO corrisponde a "user.id" dell'entity.
    // Con @Mapping esplicitiamo il percorso: source = "userId" → target = "user.id"
    // MapStruct creerà un oggetto User con solo l'id valorizzato; JPA lo userà come riferimento FK.
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "diary.id", source = "diaryId")
    @Mapping(target = "service.id", source = "serviceId")
    Booking toEntity(BookingDTO bookingDTO);

    // Da Entity → DTO (es. quando restituiamo dati al client)
    //
    // Direzione inversa: estraiamo solo l'ID dagli oggetti relazionali e lo mettiamo nel DTO.
    // Evitare di esporre l'intera entità annidata evita loop di serializzazione e dati inutili.
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "diaryId", source = "diary.id")
    @Mapping(target = "serviceId", source = "service.id")
    BookingDTO toDTO(Booking booking);

    // MapStruct genera automaticamente i metodi per le liste
    // delegando a toDTO(Booking) / toEntity(BookingDTO) per ogni elemento
    List<BookingDTO> toDTO(List<Booking> bookings);
    List<Booking> toEntity(List<BookingDTO> bookingDTOs);

}
