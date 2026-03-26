package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.model.Diary;

// Rimosso uses per evitare cicli con ActivityMapper e BookingMapper
@Mapper(componentModel = "spring")
public interface DiaryMapper 
{
    @Mapping(target = "activityId", source = "activity.id")
    DiaryDTO toDTO(Diary diary);
    List<DiaryDTO> toDTO(List<Diary> diaries);

    @Mapping(target = "activity.id", source = "activityId")
    Diary toEntity(DiaryDTO diaryDTO);
    List<Diary> toEntity(List<DiaryDTO> diaryDTOs);
}
