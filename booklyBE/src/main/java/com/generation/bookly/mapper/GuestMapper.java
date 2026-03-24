package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.model.Guest;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    
    GuestDTO toDTO(Guest guest);
    List<GuestDTO> toDTO(List<Guest> guests);

    Guest toEntity(GuestDTO guestDTO);
    List<Guest> toEntity(List<GuestDTO> guestDTOs);
}
