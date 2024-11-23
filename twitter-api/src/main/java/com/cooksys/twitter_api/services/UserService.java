package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

public interface UserService {
    /*  GET /users */
    List<UserResponseDto> findByDeletedFalse();
    /* POST /users */
    UserResponseDto createUser(UserRequestDto userRequestDto);
    /* PATCH /users/@{username} */
    UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto);
    /* GET /users/@{username} */
    UserResponseDto getUserByUsername(@PathVariable String username);
    /* DELETE /users/@{username} */
    UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto);
    /* GET /users/@{username}/following */
    List<UserResponseDto> getUsersFollowedByUsername(@PathVariable String username);
    /* GET /users@/{username}/followers */
    List<UserResponseDto> getFollowersByUsername(@PathVariable String username);
    /* GET /users/@{username}/tweets */
    List<TweetResponseDto> getTweetsByUsername(String username);
    /* GET /users/@{username}/feed */
    List<TweetResponseDto> getUserFeed(String username);
    /* GET /users/@{username}/mentions */
    List<TweetResponseDto> getMentionsByUsername(String username);
    

}


