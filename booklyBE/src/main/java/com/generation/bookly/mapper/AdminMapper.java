package com.generation.bookly.mapper;

import org.mapstruct.Mapper;

import com.generation.bookly.model.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    
    AdminDTO toDTO(Admin admin);

    Admin toEntity(AdminDTO adminDTO);

}
