package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	

}
