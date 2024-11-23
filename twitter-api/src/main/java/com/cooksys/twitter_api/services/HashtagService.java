package com.cooksys.twitter_api.services;

import java.util.List;

import com.cooksys.twitter_api.dtos.HashtagDto;

public interface HashtagService {
    
    List<HashtagDto> getAllHashtags();

    // Fetches a specific hashtag and all associated tweets - with content
    HashtagDto getTweetsByHashtagLabel(String label);

    /* GET val */

}
