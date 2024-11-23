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

    @Override
    @Transactional
    public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        // Validate the request body is not null
        if (userRequestDto == null) {
            throw new BadRequestException("Update request body must not be null.");
        }
    
        // Validate that credentials are provided
        if (userRequestDto.getCredentials() == null ||
            userRequestDto.getCredentials().getUsername() == null || 
            userRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("Valid credentials (username and password) are required.");
        }

        // Validate profile is provided
        if (userRequestDto.getProfile() == null) {
            throw new BadRequestException("Profile information must be provided in the update request.");
        }
    
        // Check if the user exists and is active
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username '%s' not found or is inactive.", username));
        }
    
        // Validate provided credentials match the user's credentials
        if (!user.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
            throw new BadRequestException("Invalid credentials. Password does not match.");
        }
    
        // Ensure the username matches the provided username
        if (!user.getCredentials().getUsername().equals(username)) {
            throw new BadRequestException(String.format("Username mismatch. Expected '%s' but got '%s'.", 
                username, userRequestDto.getCredentials().getUsername()));
        }
    
        // Preserve the joined timestamp to ensure it cannot be modified
        java.sql.Timestamp originalJoinedDate = user.getJoined();
    
        // Update profile fields if provided, allowing partial updates
        if (userRequestDto.getProfile() != null) {
            Profile updatedProfile = profileMapper.requestDtoToEntity(userRequestDto.getProfile());
    
            // Update only non-null fields to allow partial updates
            if (updatedProfile.getFirstName() != null && !updatedProfile.getFirstName().isBlank()) {
                user.getProfile().setFirstName(updatedProfile.getFirstName());
            }
            if (updatedProfile.getLastName() != null && !updatedProfile.getLastName().isBlank()) {
                user.getProfile().setLastName(updatedProfile.getLastName());
            }
            if (updatedProfile.getEmail() != null && !updatedProfile.getEmail().isBlank()) {
                user.getProfile().setEmail(updatedProfile.getEmail());
            }
        }
    
        // Save the updated user to the database
        userRepository.save(user);
    
        // Restore the original joined timestamp to ensure it has not been modified
        user.setJoined(originalJoinedDate);
    
        // Return the updated user as a response DTO
        return userMapper.entityToDto(user);
    }    

    @Override
    public UserResponseDto getUserByUsername(String username) {
        // Validate the username first
        if (username == null || username.isBlank()) {
            // Throw a BadRequestException if the username is null or blank
            throw new BadRequestException("Username cannot be null or blank.");
        }
    
        // Find the user in the database and ensure they are active (not deleted)
        User user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    
        // Throw a NotFoundException if no active user is found with the given username
        if (user == null) {
            throw new NotFoundException(String.format("No active user found with username '%s'.", username));
        }
    
        // Map and return the user entity as a response DTO
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

    @Override
    @Transactional
    public void followUser(String username, CredentialsDto credentialsDto) {
        // Validate credentials
        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Valid credentials are required to follow a user.");
        }
    
        // Find the user to be followed (must be active)
        User userToFollow = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (userToFollow == null) {
            throw new NotFoundException(String.format("User '%s' not found or inactive.", username));
        }
    
        // Find the current user based on credentials (must be active)
        User currentUser = userRepository.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
        if (currentUser == null || !currentUser.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new BadRequestException("Invalid credentials.");
        }
    
        // Check if the user is already being followed
        if (currentUser.getFollowing().contains(userToFollow)) {
            throw new BadRequestException(String.format("User '%s' is already followed.", username));
        }
    
        // Establish the follow relationship
        currentUser.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(currentUser);
    
        // Save both users to persist the relationship
        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }
    
    
    @Override
    @Transactional
    public void unfollowUser(String username, CredentialsDto credentialsDto) {
        // Validate credentials
        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Valid credentials are required to unfollow a user.");
        }
    
        // Find the user to be unfollowed (must be active)
        User userToUnfollow = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (userToUnfollow == null) {
            throw new NotFoundException(String.format("User '%s' not found or inactive.", username));
        }
    
        // Find the current user based on credentials (must be active)
        User currentUser = userRepository.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
        if (currentUser == null || !currentUser.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new BadRequestException("Invalid credentials.");
        }
    
        // Check if the user is not currently being followed
        if (!currentUser.getFollowing().contains(userToUnfollow)) {
            throw new BadRequestException(String.format("User '%s' is not currently followed.", username));
        }
    
        // Remove the follow relationship
        currentUser.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(currentUser);
    
        // Save both users to persist the changes
        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
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
