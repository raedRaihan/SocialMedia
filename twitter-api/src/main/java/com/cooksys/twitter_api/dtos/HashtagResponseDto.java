package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagResponseDto {
    private Long id;  // optional - useful internally
    private String label;
    private Timestamp firstUsed;
    private Timestamp lastUsed;
    private List<TweetResponseDto> tweets; // optional - for providing associated tweets
}
