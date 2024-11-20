package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class HashtagDto {
    private String label; // For creating or updating a hashtag

    private Timestamp firstUsed;

    private Timestamp lastUsed;
}
