package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    // Converts a Hashtag entity to a HashtagDto
    HashtagDto entityToDto(Hashtag entity);

    // Converts a list of Hashtag entities to a list of HashtagDto
    List<HashtagDto> entitiesToDtos(List<Hashtag> entities);
}
