package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Tweet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    // Basic repository; add derived queries when needed
	List<Tweet> findAllByDeletedFalse();

	Optional<Tweet> findByIdAndDeletedFalse(Long id);
	
}
