/*package com.cooksys.twitter_api.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;

    @Override
    public boolean usernameExists(String username) {
        // Delegate the logic to the UserRepository
        return userRepository.existsByCredentialsUsernameAndDeletedFalse(username);
    }
}*/