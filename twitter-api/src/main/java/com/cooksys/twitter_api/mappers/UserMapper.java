package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Converts a UserRequestDto (POST || PATCH) to a User entity for saving to the database
    User requestDtoToEntity(UserRequestDto dto);

    // Converts a User entity to a UserResponseDto (GET || DELETE) for returning data to the client
    UserResponseDto entityToDto(User entity);

    // Converts a list of User entities to a list of UserResponseDto
    List<UserResponseDto> entitiesToDtos(List<User> entities);
}