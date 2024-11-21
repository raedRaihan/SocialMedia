package com.cooksys.twitter_api.mappers;

import java.util.List;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

    // Converts a TweetRequestDto to a Tweet entity
    Tweet requestDtoToEntity(TweetRequestDto dto);

    // Converts a Tweet entity to a TweetResponseDto
    TweetResponseDto entityToDto(Tweet entity);

    // Converts a list of Tweet entities to a list of TweetResponseDto
    List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);
}
