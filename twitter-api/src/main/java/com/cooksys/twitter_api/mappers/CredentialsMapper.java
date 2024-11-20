package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    // Converts a CredentialsRequestDto (POST || PATCH) to a Credentials entity for saving to the database
    Credentials requestDtoToEntity(CredentialsDto dto);
}
