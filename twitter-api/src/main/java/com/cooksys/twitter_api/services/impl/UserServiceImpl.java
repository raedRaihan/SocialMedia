package com.cooksys.twitter_api.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Credentials;
import com.cooksys.twitter_api.entities.Profile;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
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
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    // Fetch all active (non-deleted) users and return as DTOs
    @Override
    public List<UserResponseDto> findByDeletedFalse() {
        List<User> activeUsers = userRepository.findByDeletedFalse();
        return userMapper.entitiesToDtos(activeUsers); // Convert to DTOs and return
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
        if (userRequestDto == null || 
        (userRequestDto.getCredentials().getUsername() == null && userRequestDto.getProfile() == null)) {
            throw new BadRequestException("Update request must include valid fields to update");
}
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

    @Override
    public UserResponseDto getUserByUsername(@PathVariable String username) {
    
        // Validate the username first
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username can't be null or blank");
        }
    
        // Find the user in the database
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    
        // Check if the user exists
        if (user == null) {
            throw new BadRequestException(String.format("User with username '%s' not found or is deleted.", username));
        }
    
        // Map and return the user entity as a DTO
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
        // Validate credentials in the request body
        if (credentialsDto == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Credentials are required for deleting a user");
        }
    
        // Find the user by username (ensure not already deleted)
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username '%s' not found or is already deleted.", username));
        }
    
        // Validate credentials
        if (!user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials for user deletion");
        }
    
        // Soft delete the user
        user.setDeleted(true);
        userRepository.save(user);
    
        // Return the user data prior to deletion
        return userMapper.entityToDto(user);
    }

    @Override
    public List<UserResponseDto> getUsersFollowedByUsername(String username) {
        // Validate the username
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank");
        }

        // Find the user by username and ensure they are active
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username '%s' not found or is inactive.", username));
        }

        // Filter the followed users to include only active ones
        List<User> followedUsers = user.getFollowing().stream()
            .filter(followedUser -> !followedUser.isDeleted()) // Ensure the followed user is active
            .collect(Collectors.toList());

        // Map the filtered users to response DTOs and return
        return userMapper.entitiesToDtos(followedUsers);
    }

    @Override
    public List<UserResponseDto> getFollowersByUsername(String username) {
        // Validate the username
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank");
        }

        // Find the user by username and ensure they are active
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username '%s' not found or is inactive.", username));
        }

        // Filter the followers to include only active ones
        List<User> activeFollowers = user.getFollowers().stream()
            .filter(follower -> !follower.isDeleted()) // Ensure the follower is active
            .collect(Collectors.toList());

        // Map the filtered followers to response DTOs and return
        return userMapper.entitiesToDtos(activeFollowers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponseDto> getTweetsByUsername(String username) {
        // Validate username
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank.");
        }
    
        // Find user by username
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("No active user found with username '%s'.", username));
        }
    
        // Fetch all tweets authored by the user
        List<Tweet> userTweets = tweetRepository.findByAuthorAndDeletedFalseOrderByTimestampDesc(user);
    
        // Map tweets to response DTOs and return
        return userTweets.stream()
                .map(tweetMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponseDto> getUserFeed(String username) {
        // Validate the username
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank.");
        }
    
        // Find the user by username and ensure they are active
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("No active user found with username '%s'.", username));
        }
    
        // Fetch all tweets authored by the user
        List<Tweet> userTweets = tweetRepository.findByAuthorAndDeletedFalseOrderByTimestampDesc(user);
    
        // Fetch all tweets authored by users the given user is following
        List<Tweet> followingTweets = user.getFollowing().stream()
                .filter(followedUser -> !followedUser.isDeleted()) // Exclude deleted users
                .flatMap(followedUser -> tweetRepository.findByAuthorAndDeletedFalseOrderByTimestampDesc(followedUser).stream())
                .collect(Collectors.toList());
    
        // Combine user tweets and following tweets
        List<Tweet> allTweets = Stream.concat(userTweets.stream(), followingTweets.stream())
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // Reverse order by timestamp
                .collect(Collectors.toList());
    
        // Map tweets to response DTOs and return
        return allTweets.stream()
                .map(tweetMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponseDto> getMentionsByUsername(String username) {
        // Validate username
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be null or blank.");
        }

        // Find user by username
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("No active user found with username '%s'.", username));
        }

        // Fetch all tweets mentioning the user
        List<Tweet> mentionedTweets = tweetRepository.findByMentionedUsersContainingAndDeletedFalseOrderByTimestampDesc(user);

        // Map tweets to response DTOs and return
        return mentionedTweets.stream()
                .map(tweetMapper::entityToDto)
                .collect(Collectors.toList());
    }
    
    
    

    /* Potential Helper function for validation
     * public void validateRequiredFields(UserRequestDto userRequestDto) {
        if (userRequestDto == null || userRequestDto.getCredentials() == null ||
            userRequestDto.getCredentials().getUsername() == null || 
            userRequestDto.getCredentials().getPassword() == null ||
            userRequestDto.getProfile() == null ||
            userRequestDto.getProfile().getEmail() == null) {
            throw new BadRequestException("Missing required fields");
        }
    }

     */


}
