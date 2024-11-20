package com.cooksys.twitter_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContextDto {
    private TweetResponseDto target; // Represents the target tweet
    private List<TweetResponseDto> before; // List of tweets in the chain of replies before the target, in chronological order
    private List<TweetResponseDto> after; // List of tweets in the chain of replies after the target, flattened and in chronological order
}

