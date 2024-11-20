package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id; // Unique identifier for the tweet
    private UserResponseDto author; // The user who posted the tweet
    private Timestamp posted; // Timestamp of when the tweet was created
    private String content; // Content of the tweet (optional, depends on the type of tweet)
    private TweetResponseDto inReplyTo; // If the tweet is a reply, references the original tweet
    private TweetResponseDto repostOf; // If the tweet is a repost, references the original tweet
}

