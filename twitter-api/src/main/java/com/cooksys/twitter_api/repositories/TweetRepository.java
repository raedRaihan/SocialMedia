package com.cooksys.twitter_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Custom query to fetch tweets by hashtag, not deleted, ordered by timestamp descending
    @Query("SELECT t FROM Tweet t JOIN t.hashtags h WHERE h = :hashtag AND t.deleted = false ORDER BY t.timestamp DESC")
    List<Tweet> findAllByHashtagAndDeletedFalseOrderByTimestampDesc(@Param("hashtag") Hashtag hashtag);
}
