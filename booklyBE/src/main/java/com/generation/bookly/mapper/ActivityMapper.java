package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.model.Activity;

@Mapper(componentModel = "spring", uses={BookingMapper.class, DiaryMapper.class, ServiceMapper.class})
public interface ActivityMapper {

        ActivityDTO toDTO(Activity activity);
        List<ActivityDTO> toDTO(List<Activity> activities);
    
        Activity toEntity(ActivityDTO activityDTO);
        List<Activity> toEntity(List<ActivityDTO> activityDTOs);
}
