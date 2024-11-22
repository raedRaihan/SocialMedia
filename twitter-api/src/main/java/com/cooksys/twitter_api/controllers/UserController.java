package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.services.UserService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllActiveUsers() {
        return userService.findByDeletedFalse();  // Call the service method to fetch users
    }

    /*public String getMethodName(@RequestParam String param) {
        return new String();
    }  */  

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }
  
    @PatchMapping("/@{username}")
    public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateProfile(username, userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(
            @PathVariable String username,
            @RequestBody CredentialsDto credentialsDto) {
        return userService.deleteUser(username, credentialsDto);
    }

    @PostMapping("/@{username}/follow")
    public UserResponseDto followUser(
            @PathVariable String username,
            @RequestBody CredentialsDto credentialsDto) {
        return userService.followUser(username, credentialsDto);
    }

    @PostMapping("/@{username}/unfollow")
    public UserResponseDto unFollowUser(
            @PathVariable String username,
            @RequestBody CredentialsDto credentialsDto) {
        return userService.unFollowUser(username, credentialsDto);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        // Delegating to the service layer to fetch user tweets
        return userService.getUserTweets(username);
    }

    @GetMapping("/@{username}/mentions")
    public ResponseEntity<List<TweetResponseDto>> getUserMentions(@PathVariable String username) {
        // Call the service method to get mentions for the user
        List<TweetResponseDto> mentions = userService.getUserMentions(username);
        
        // Return the mentions in a ResponseEntity
        return ResponseEntity.ok(mentions);
    }
    

    

    

}

    /* Helper Function */
    /*private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank");
        }
        // Remove '@' at the start of the username if it exists
        return username.startsWith("@") ? username.substring(1) : username;
    }*/




