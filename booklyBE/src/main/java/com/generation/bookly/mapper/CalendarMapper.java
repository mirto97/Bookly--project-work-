package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.model.Calendar;

@Mapper(componentModel = "spring", uses={ActivityMapper.class})
public interface CalendarMapper {

    CalendarDTO toDTO(Calendar calendar);
    List<CalendarDTO> toDTO(List<Calendar> calendars);

    Calendar toEntity(CalendarDTO calendarDTO);
    List<Calendar> toEntity(List<CalendarDTO> calendarDTOs);
}
