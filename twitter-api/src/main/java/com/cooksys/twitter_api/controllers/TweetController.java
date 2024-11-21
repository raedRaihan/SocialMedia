package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    
	private final TweetService tweetService;
	
	// GET tweets #60

	@GetMapping
	public List<TweetResponseDto> getAllTweets()
	{
		return tweetService.getAllTweets();
	}
	
	// GET tweets/{id} #58
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id)
	{
		return tweetService.getTweetById(id);
	}
	
	// DELETE tweets/{id} #57
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto)
	{
		return tweetService.deleteTweetById(id,tweetRequestDto);
	}

	

}
