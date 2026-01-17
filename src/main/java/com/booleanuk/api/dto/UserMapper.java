package com.booleanuk.api.dto;

import com.booleanuk.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
}
