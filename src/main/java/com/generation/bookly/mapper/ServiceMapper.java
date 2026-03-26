package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.dto.ServiceDTO;
import com.generation.bookly.model.Service;

@Mapper(componentModel = "spring", uses={ActivityMapper.class, BookingMapper.class, DiaryMapper.class})
public interface ServiceMapper 
{
    
    ServiceDTO toDTO(Service service);
    List<ServiceDTO> toDTO(List<Service> services);

    Service toEntity(ServiceDTO serviceDTO);
    List<Service> toEntity(List<ServiceDTO> serviceDTOs);

}
