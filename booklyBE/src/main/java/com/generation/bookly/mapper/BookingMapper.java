package com.generation.bookly.mapper;

import org.mapstruct.Mapper;

import com.generation.bookly.model.Booking;

@Mapper(componentModel = "spring", uses={GuestMapper.class}, uses={ServiceMapper.class}, uses={CalendarMapper.class}, uses={ServiceMapper.class})
public interface BookingMapper {
    
    BookingDTO toDTO(Booking booking);
    List<BookingDTO> toDTO(List<Booking> bookings);

    Booking toEntity(BookingDTO bookingDTO);
    List<Booking> toEntity(List<BookingDTO> bookingDTOs);

}
