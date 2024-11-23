package com.cooksys.twitter_api.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean usernameExists(String username) {
        // Check if a non-deleted user with the given username exists
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank.");
        }
        return userRepository.existsByCredentialsUsernameAndDeletedFalse(username);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        // A username is available if it does not exist in the database or belongs to a deleted user
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank.");
        }
        // Check for availability based on both active and deleted users
        return userRepository.findByCredentialsUsernameAndDeletedFalse(username) == null &&
               userRepository.findByCredentialsUsernameAndDeletedTrue(username) == null;
    }

    @Override
    public boolean doesTagExist(String label) {
        // A tag exists if a hashtag with the given label is found in the database
        if (label == null || label.isBlank()) {
            throw new BadRequestException("Hashtag label cannot be null or blank.");
        }
        return hashtagRepository.existsByLabelIgnoreCase(label);
    }
}