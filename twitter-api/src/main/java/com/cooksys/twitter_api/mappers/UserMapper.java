package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

    // Converts a User entity to a UserResponseDto
    @Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User entity);

    // Converts a UserRequestDto to a User entity
    User requestDtoToEntity(UserRequestDto dto);

    // Converts a list of User entities to a list of UserResponseDto
    List<UserResponseDto> entitiesToDtos(List<User> entities);
}
