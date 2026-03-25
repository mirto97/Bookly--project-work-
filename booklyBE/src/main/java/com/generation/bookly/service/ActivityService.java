package com.generation.bookly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.bookly.dto.ActivityDTO;
import com.generation.bookly.mapper.ActivityMapper;
import com.generation.bookly.model.Activity;
import com.generation.bookly.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public List<ActivityDTO> getAllApprovedActivities() {
        return activityRepository.findByStatus(Activity.ActivityStatus.APPROVED)
                .stream()
                .map(activityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActivityDTO registerActivity(ActivityDTO dto) {
        Activity activity = activityMapper.toEntity(dto);
        activity.setStatus(Activity.ActivityStatus.PENDING); // In attesa di approvazione admin
        return activityMapper.toDTO(activityRepository.save(activity));
    }
}