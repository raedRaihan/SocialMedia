package com.cooksys.twitter_api.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Credentials;
import com.cooksys.twitter_api.entities.Profile;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final CredentialsMapper credentialsMapper;

    // Fetch all active (non-deleted) users and return as DTOs
    @Override
    public List<UserResponseDto> findByDeletedFalse() {
        List<User> activeUsers = userRepository.findByDeletedFalse();
        return userMapper.entitiesToDtos(activeUsers); // Convert to DTOs and return
    }

    // validate if a username exists
    @Override
    public boolean usernameExists(String username) {
        // database query - if username exists it is not null
        return userRepository.findByCredentialsUsernameAndDeletedFalse(username) != null;
    }

    // Post new user
    @Override
    @Transactional // Ensures all changes are committed together or rolled back on failure
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Validate required fields
        if (userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null ||
            userRequestDto.getCredentials().getPassword() == null || userRequestDto.getProfile() == null ||
            userRequestDto.getProfile().getEmail() == null) {
            throw new BadRequestException("Missing required fields");
        }
    
        String username = userRequestDto.getCredentials().getUsername();
    
        // Check if the username is already taken by an active user
        User activeUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (activeUser != null) {
            throw new BadRequestException("Username is already taken");
        }
    
        // Check if a previously deleted user exists
        User deletedUser = userRepository.findByCredentialsUsernameAndDeletedTrue(username);
        User revivedUser;
    
        if (deletedUser != null) {
            // Reactivate the user
            deletedUser.setDeleted(false);
    
            // Map CredentialsDto to Credentials and update user credentials
            Credentials newCredentials = credentialsMapper.requestDtoToEntity(userRequestDto.getCredentials());
            deletedUser.setCredentials(newCredentials);
    
            // Map ProfileDto to Profile and update user profile
            Profile newProfile = profileMapper.requestDtoToEntity(userRequestDto.getProfile());
            deletedUser.setProfile(newProfile);
    
            // Save the updated user back to the database
            revivedUser = userRepository.save(deletedUser);
    
        } else {
            // Create a new user
            revivedUser = userMapper.requestDtoToEntity(userRequestDto);
    
            // Ensure the user is marked as active
            revivedUser.setDeleted(false);
    
            // Save the new user to the database
            revivedUser = userRepository.save(revivedUser);
        }
    
        // Convert the saved User entity to a response DTO and return it
        return userMapper.entityToDto(revivedUser);
    }

    // Patch user
    @Override
    @Transactional
    public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        // first validate that the request body has needed fields
        if (userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null ||
            userRequestDto.getCredentials().getPassword() == null || userRequestDto.getProfile() == null) {
            
            throw new BadRequestException("Invalid Credentials");
        }

        // check if the user exists and is active
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new BadRequestException("User not found or deleted / not active");
        }

        // validate the provided credentials match the user's credentials
        Credentials providedCredentials = credentialsMapper.requestDtoToEntity(userRequestDto.getCredentials());
        if (!user.getCredentials().getPassword().equals(providedCredentials.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        //  update the user's profile with the new username
        Profile updatedProfile = profileMapper.requestDtoToEntity(userRequestDto.getProfile());
        user.setProfile(updatedProfile);

        // save the updated user to the db
        User updatedUser = userRepository.save(user); 

        return userMapper.entityToDto(updatedUser);
    }
    


}
