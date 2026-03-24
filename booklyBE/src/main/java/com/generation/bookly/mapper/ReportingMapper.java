package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.model.Reporting;

@Mapper(componentModel = "spring", uses={GuestMapper.class})
public interface ReportingMapper {
    
    ReportingDTO toDTO(Reporting reporting);
    List<ReportingDTO> toDTO(List<Reporting> reportings);

    Reporting toEntity(ReportingDTO reportingDTO);
    List<Reporting> toEntity(List<ReportingDTO> reportingDTOs);
    
}
