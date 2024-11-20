package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    // Converts a Hashtag entity to a HashtagResponseDto (GET || DELETE) for returning data to the client
    HashtagDto entityToDto(Hashtag entity);

    // Converts a list of Hashtag entities to a list of HashtagResponseDto
    List<HashtagDto> entitiesToDtos(List<Hashtag> entities);
}
