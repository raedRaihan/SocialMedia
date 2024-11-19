package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
    private String username; // Matches `username: 'string'` in the JSON structure
    private ProfileResponseDto profile; // Matches `profile: 'Profile'` in the JSON structure
    private Timestamp joined; // Matches `joined: 'timestamp'` in the JSON structure
}

