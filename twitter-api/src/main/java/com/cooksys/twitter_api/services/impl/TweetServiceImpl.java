package com.cooksys.twitter_api.services.impl;


import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
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
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
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

	@Override
	public TweetResponseDto deleteTweetById(Long id, TweetRequestDto tweetRequestDto)
	{
		Optional<Tweet> optionalTweets= tweetRepository.findByIdAndDeletedFalse(id);
		
		
		if(optionalTweets.isEmpty())
		{
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST,"No Tweet found with id: "+id);
		}
		Tweet tweetToDelete=optionalTweets.get();
		
		// check if credentials are correct
		User tweetAuthor=tweetToDelete.getAuthor();
		
		
		if(tweetAuthor.getCredentials().getPassword().equals(tweetRequestDto.getCredentials().getPassword()) == false)
		{
			throw new ResponseStatusException (HttpStatus.UNAUTHORIZED,"Invalid Login");
		}
		
		
		
		tweetToDelete.setDeleted(true);
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
		
	}

	@Override
	public TweetResponseDto createReplyToTweet(Long id, TweetRequestDto tweetRequestDto) {
		
		Optional<Tweet> optionalTweet= tweetRepository.findByIdAndDeletedFalse(id);
		
		
		if(optionalTweet.isEmpty())
		{
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST,"No Tweet found with id: "+id);
		}
		Tweet tweetToReplyTo=optionalTweet.get();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		
		
		
		if(tweetRequestDto.getContent()== null || tweetRequestDto.getCredentials().getUsername()==null || tweetRequestDto.getCredentials().getPassword()==null)
		{
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST,"Make sure you fill out the content and credential fields");
		}
		
		List<User> allUsers=userRepository.findAll();

		
		
		User foundAuthor=null;
		
		for(User u:allUsers)
		{
			
			if((u.getCredentials().getUsername().equals(tweetRequestDto.getCredentials().getUsername())==true) && (u.getCredentials().getPassword().equals(tweetRequestDto.getCredentials().getPassword())==true) )
			{
				
				foundAuthor=u;
				
			}
		}
		
		if(foundAuthor==null)
		{
			throw new ResponseStatusException (HttpStatus.UNAUTHORIZED,"Invalid Credentials");
		}
		
		Tweet newTweet=new Tweet();
		
		newTweet.setContent(tweetRequestDto.getContent());
		newTweet.setAuthor(foundAuthor);
		
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
	}
	
	
	
   
}
