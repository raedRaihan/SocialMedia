package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    // Converts a ProfileDto to a Profile entity
    Profile requestDtoToEntity(ProfileDto dto);

    // Converts a Profile entity to a ProfileDto
    ProfileDto entityToDto(Profile entity);

    // Converts a list of Profile entities to a list of ProfileDto
    List<ProfileDto> entitiesToDtos(List<Profile> entities);
}
