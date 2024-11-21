package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/validate/username/exists/@{username}") 
        public boolean usernameExists(@PathVariable String username) {
            //username = normalizeUsername(username);
            return userService.usernameExists(username);
        }
    
    public String getMethodName(@RequestParam String param) {
        return new String();
    }    

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateProfile(username, userRequestDto);
    }

    /* Helper Function */
    /*private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank");
        }
        // Remove '@' at the start of the username if it exists
        return username.startsWith("@") ? username.substring(1) : username;
    }*/



}
