package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.dto.DiaryDTO;
import com.generation.bookly.model.Diary;

@Mapper(componentModel = "spring", uses={ActivityMapper.class, BookingMapper.class})
public interface DiaryMapper 
{

    DiaryDTO toDTO(Diary diary);
    List<DiaryDTO> toDTO(List<Diary> diaries);

    Diary toEntity(DiaryDTO diaryDTO);
    List<Diary> toEntity(List<DiaryDTO> diaryDTOs);
}
