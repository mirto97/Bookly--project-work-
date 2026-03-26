package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.generation.bookly.dto.UserDTO;
import com.generation.bookly.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper 
{

    UserDTO toDTO(User user);
    List<UserDTO> toDTO(List<User> users);

    User toEntity(UserDTO userDTO);
    List<User> toEntity(List<UserDTO> userDTOs);
}
