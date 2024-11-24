package com.cooksys.twitter_api.services;

import java.util.List;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;

public interface HashtagService {
    
    List<HashtagDto> getAllHashtags();

    // Fetches a specific hashtag and all associated tweets - with content
    List<TweetResponseDto> getTweetsByHashtagLabel(String label);

    /* GET val */

}
