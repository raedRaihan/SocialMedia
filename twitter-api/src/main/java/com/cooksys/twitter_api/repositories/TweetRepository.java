package com.cooksys.twitter_api.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Basic repository; add derived queries when needed
	List<Tweet> findAllByDeletedFalse();

	Optional<Tweet> findByIdAndDeletedFalse(Long id);
	
	// Custom query to fetch tweets by hashtag, not deleted, ordered by timestamp descending
    @Query("SELECT t FROM Tweet t JOIN t.hashtags h WHERE h = :hashtag AND t.deleted = false ORDER BY t.timestamp DESC")
    List<Tweet> findAllByHashtagAndDeletedFalseOrderByTimestampDesc(@Param("hashtag") Hashtag hashtag);

    // Custom query to fetch tweets authored by a specific user, not deleted, ordered by timestamp descending
    @Query("SELECT t FROM Tweet t WHERE t.author = :author AND t.deleted = false ORDER BY t.timestamp DESC")
    List<Tweet> findByAuthorAndDeletedFalseOrderByTimestampDesc(@Param("author") User author);

    // Custom query to fetch tweets mentioning a specific user in reverse-chronological order
    @Query("SELECT t FROM Tweet t JOIN t.mentionedUsers u WHERE u = :user AND t.deleted = false ORDER BY t.timestamp DESC")
    List<Tweet> findByMentionedUsersContainingAndDeletedFalseOrderByTimestampDesc(@Param("user") User user);

}
