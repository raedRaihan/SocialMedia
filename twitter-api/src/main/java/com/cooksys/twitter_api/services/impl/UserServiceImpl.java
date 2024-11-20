package com.cooksys.twitter_api.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Fetch all active (non-deleted) users and return as DTOs
    @Override
    public List<UserResponseDto> findByDeletedFalse() {
        List<User> activeUsers = userRepository.findByDeletedFalse();
        return userMapper.entitiesToDtos(activeUsers); // Convert to DTOs and return
    }


}
