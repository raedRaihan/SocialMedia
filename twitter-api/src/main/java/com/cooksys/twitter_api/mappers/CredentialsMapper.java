package com.cooksys.twitter_api.mappers;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    // Converts a CredentialsDto to a Credentials entity
    Credentials requestDtoToEntity(CredentialsDto dto);

    // Converts a Credentials entity to a CredentialsDto
    CredentialsDto entityToDto(Credentials entity);
}
