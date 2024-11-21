package com.cooksys.twitter_api.services.impl;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.TweetService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService
{
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
	}
	
	
	
   
}
