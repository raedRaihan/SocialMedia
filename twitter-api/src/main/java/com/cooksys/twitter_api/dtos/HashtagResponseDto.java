package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagResponseDto {
    private String label;
    private Timestamp firstUsed;
    private Timestamp lastUsed;
}
