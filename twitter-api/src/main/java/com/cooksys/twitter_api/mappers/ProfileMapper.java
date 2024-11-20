package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    // Converts a ProfileRequestDto (POST || PATCH) to a Profile entity for saving to the database
    Profile requestDtoToEntity(ProfileDto dto);

    // Converts a Profile entity to a ProfileResponseDto (GET || DELETE) for returning data to the client
    ProfileDto entityToDto(Profile entity);

    // Converts a list of Profile entities to a list of ProfileResponseDto
    List<ProfileDto> entitiesToDtos(List<Profile> entities);
}
