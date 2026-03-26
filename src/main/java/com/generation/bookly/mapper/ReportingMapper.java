package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.ReportingDTO;
import com.generation.bookly.model.Reporting;

@Mapper(componentModel = "spring", uses={UserMapper.class})
public interface ReportingMapper 
{
    
    @Mapping(source = "author.id", target = "authorId")
    ReportingDTO toDTO(Reporting reporting);
    List<ReportingDTO> toDTO(List<Reporting> reportings);
    
    @Mapping(source = "authorId", target = "author.id")
    Reporting toEntity(ReportingDTO reportingDTO);
    List<Reporting> toEntity(List<ReportingDTO> reportingDTOs);
    
}
