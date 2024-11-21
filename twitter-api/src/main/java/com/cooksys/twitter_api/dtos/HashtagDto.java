package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagDto {
    private String label; // For creating or updating a hashtag

    private Timestamp firstUsed;

    private Timestamp lastUsed;

    private List<TweetResponseDto> tweets; // Associated tweets with this hashtag
}
