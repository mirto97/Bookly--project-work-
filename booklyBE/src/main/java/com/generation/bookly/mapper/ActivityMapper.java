package com.generation.bookly.mapper;

import java.util.List;

import com.generation.bookly.model.Activity;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

        ActivityDTO toDTO(Activity activity);
        List<ActivityDTO> toDTO(List<Activity> activities);
    
        Activity toEntity(ActivityDTO activityDTO);
        List<Activity> toEntity(List<ActivityDTO> activityDTOs);
}
