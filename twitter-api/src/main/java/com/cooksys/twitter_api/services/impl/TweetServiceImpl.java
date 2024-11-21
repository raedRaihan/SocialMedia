package com.cooksys.twitter_api.services.impl;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.TweetService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

	@Override
	public TweetResponseDto getTweetById(Long id) {
		
		Optional<Tweet> optionalTweets= tweetRepository.findByIdAndDeletedFalse(id);
		
		if(optionalTweets.isEmpty())
		{
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST,"No Tweet found with id: "+id);
		}
		
		Tweet tweetToReturn = optionalTweets.get();
		
		return tweetMapper.entityToDto(tweetToReturn);
		
	}
	
	
	
   
}
