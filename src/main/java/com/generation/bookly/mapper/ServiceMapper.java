package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.ServiceDTO;
import com.generation.bookly.model.Service;

@Mapper(componentModel = "spring", uses={ActivityMapper.class, BookingMapper.class, DiaryMapper.class})
public interface ServiceMapper 
{
    
    @Mapping(target = "activityId", source = "activity.id") // <--- AGGIUNGI QUESTA RIGA
    ServiceDTO toDTO(Service service);
    
    List<ServiceDTO> toDTO(List<Service> services);

    @Mapping(target = "activity.id", source = "activityId") // <--- AGGIUNGI ANCHE QUESTA PER IL RITORNO
    Service toEntity(ServiceDTO serviceDTO);
    
    List<Service> toEntity(List<ServiceDTO> serviceDTOs);

}
