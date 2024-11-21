package com.cooksys.twitter_api.services;

import java.util.List;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();
    // Define methods here later

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweetById(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto createReplyToTweet(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	List<HashtagDto> getTweetTags(Long id);

	List<UserResponseDto> getMentionedUsers(Long id);
}
