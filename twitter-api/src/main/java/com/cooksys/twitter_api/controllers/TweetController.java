package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

	private final TweetService tweetService;

	// GET tweets #60

	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}

	// POST tweets #59
	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}

	// GET tweets/{id} #58
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}

	// DELETE tweets/{id} #57
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweetById(id, credentialsDto);
	}

	// POST tweets/{id}/reply #55
	@PostMapping("/{id}/reply")
	public TweetResponseDto createReplyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createReplyToTweet(id, tweetRequestDto);
	}

	// GET tweets/{id}/tags #53
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTweetTags(@PathVariable Long id) {
		return tweetService.getTweetTags(id);
	}

	// GET tweets/{id}/mentions #48
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getMentionedUsers(@PathVariable Long id) {
		return tweetService.getMentionedUsers(id);
	}

	// POST tweets/{id}/like #56
	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto);
	}

	// GET tweets/{id}/likes #52
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getUsersWhoLikedTweet(@PathVariable Long id) {
		return tweetService.getUsersWhoLikedTweet(id);
	}

	// GET tweets/{id}/context #51
	@GetMapping("/{id}/context")
	public ContextDto getContextOfTweet(@PathVariable Long id) {
		return tweetService.getContextOfTweet(id);
	}

	// GET tweets/{id}/replies #50
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getRepliesToTweet(@PathVariable Long id) {
		return tweetService.getRepliesToTweet(id);
	}

	// POST tweets/{id}/repost #54
	@PostMapping("/{id}/repost")
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.repostTweet(id, credentialsDto);
	}

	// GET tweets/{id}/reposts #49
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getRepostOfTweet(@PathVariable Long id) {
		return tweetService.getRepostOfTweet(id);
	}

}
