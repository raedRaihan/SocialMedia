package com.cooksys.twitter_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.entities.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JPA's query method naming convention will convert to proper SQL
    List<User> findByDeletedFalse();

    // Find an active user by username (deleted = false)
    User findByCredentialsUsernameAndDeletedFalse(String username);

    // Find a previously deleted user by username (deleted = true)
    User findByCredentialsUsernameAndDeletedTrue(String username);
}

