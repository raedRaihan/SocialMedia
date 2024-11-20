package com.cooksys.twitter_api.mappers;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    // Converts a CredentialsRequestDto (POST || PATCH) to a Credentials entity for saving to the database
    Credentials requestDtoToEntity(CredentialsDto dto);

    // Converts a Credentials entity to a CredentialsDto for sending in a response
    CredentialsDto entityToDto(Credentials entity);
}
