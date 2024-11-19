package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.ProfileRequestDto;
import com.cooksys.twitter_api.dtos.ProfileResponseDto;
import com.cooksys.twitter_api.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    // Converts a ProfileRequestDto (POST || PATCH) to a Profile entity for saving to the database
    Profile requestDtoToEntity(ProfileRequestDto dto);

    // Converts a Profile entity to a ProfileResponseDto (GET || DELETE) for returning data to the client
    ProfileResponseDto entityToDto(Profile entity);

    // Converts a list of Profile entities to a list of ProfileResponseDto
    List<ProfileResponseDto> entitiesToDtos(List<Profile> entities);
}
