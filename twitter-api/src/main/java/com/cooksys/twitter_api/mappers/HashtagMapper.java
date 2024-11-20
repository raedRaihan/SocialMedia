package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.HashtagRequestDto;
import com.cooksys.twitter_api.dtos.HashtagResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    // Converts a HashtagRequestDto (POST || PATCH) to a Hashtag entity for saving to the database
    Hashtag requestDtoToEntity(HashtagRequestDto dto);

    // Converts a Hashtag entity to a HashtagResponseDto (GET || DELETE) for returning data to the client
    HashtagResponseDto entityToDto(Hashtag entity);

    // Converts a list of Hashtag entities to a list of HashtagResponseDto
    List<HashtagResponseDto> entitiesToDtos(List<Hashtag> entities);
}
