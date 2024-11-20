package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    // Converts a CredentialsRequestDto (POST || PATCH) to a Credentials entity for saving to the database
    Credentials requestDtoToEntity(CredentialsDto dto);

    // Converts a Credentials entity to a CredentialsResponseDto (GET || DELETE) for returning data to the client
    CredentialsDto entityToDto(Credentials entity);

    // Converts a list of Credentials entities to a list of CredentialsResponseDto
    List<CredentialsDto> entitiesToDtos(List<Credentials> entities);
}
