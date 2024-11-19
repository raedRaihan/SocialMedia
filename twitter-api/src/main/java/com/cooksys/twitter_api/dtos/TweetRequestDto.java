package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    private String content; // Content of the tweet (optional for reposts)
    private CredentialsRequestDto credentials; // Credentials for authentication
}

