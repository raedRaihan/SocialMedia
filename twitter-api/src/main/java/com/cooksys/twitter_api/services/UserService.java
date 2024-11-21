package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

public interface UserService {
    /*  GET /users */
    List<UserResponseDto> findByDeletedFalse();
    /* GET /validate/username/exists/@{username} */
    boolean usernameExists(String username);
    /* POST /users */
    UserResponseDto createUser(UserRequestDto userRequestDto);
    /* PATCH /users/@{username} */
    UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto);
}
