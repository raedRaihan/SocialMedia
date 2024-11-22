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
    public UserResponseDto getUserByUsername(@PathVariable String username);
    /* DELETE /users/@{username} */
    UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto);
    /* POST /user/@{username}/follow */
    UserResponseDto followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto);
    /* POST /user/@{username}/unfollow */
    UserResponseDto unFollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto);
    /* GET users/@{username}/tweets*/
    List<TweetResponseDto> getUserTweets(@PathVariable String username);

    List<TweetResponseDto> getUserMentions(@PathVariable String username);

}


