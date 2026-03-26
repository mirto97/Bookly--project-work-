package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.dto.BookingDTO;
import com.generation.bookly.model.Booking;

@Mapper(componentModel = "spring", uses = {ActivityMapper.class, UserMapper.class, ServiceMapper.class})
public interface BookingMapper 
{
    
    BookingDTO toDTO(Booking booking);
    List<BookingDTO> toDTO(List<Booking> bookings);

    Booking toEntity(BookingDTO bookingDTO);
    List<Booking> toEntity(List<BookingDTO> bookingDTOs);

}
