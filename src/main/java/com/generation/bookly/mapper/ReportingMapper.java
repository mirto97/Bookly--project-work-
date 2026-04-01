package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.model.Reporting;

@Mapper(componentModel = "spring", uses={UserMapper.class})
public interface ReportingMapper 
{
    // Da Entity a DTO
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "booking.id", target = "bookingId") // AGGIUNTO
    ReportingDTO toDTO(Reporting reporting);
    
    List<ReportingDTO> toDTO(List<Reporting> reportings);
    
    // Da DTO a Entity
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "bookingId", target = "booking.id") // AGGIUNTO
    Reporting toEntity(ReportingDTO reportingDTO);
    
    List<Reporting> toEntity(List<ReportingDTO> reportingDTOs);
}
