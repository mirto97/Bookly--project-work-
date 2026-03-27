package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.model.Activity;

@Mapper(componentModel = "spring", uses={BookingMapper.class, DiaryMapper.class, ServiceMapper.class})
public interface ActivityMapper
{

        // Gabri: Ho aggiunto questo @Mapping perché Activity ha un campo owner (oggetto User)
        // ma ActivityDTO ha solo ownerId (Integer). MapStruct non sa fare questa conversione
        // da solo, quindi gli dico esplicitamente: prendi owner.id e mettilo in ownerId.
        @Mapping(source = "owner.id", target = "ownerId")
        ActivityDTO toDTO(Activity activity);
        List<ActivityDTO> toDTO(List<Activity> activities);

        // Gabri: Ho aggiunto ignore = true su owner perché nella direzione inversa (DTO → Entity)
        // MapStruct non può caricare un User dal DB partendo solo dall'id.
        // Il caricamento viene fatto manualmente nel service (ActivityService.registerActivity).
        @Mapping(target = "owner", ignore = true)
        Activity toEntity(ActivityDTO activityDTO);
        List<Activity> toEntity(List<ActivityDTO> activityDTOs);
}
