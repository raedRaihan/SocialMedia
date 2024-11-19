package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    // Basic repository; add derived queries when needed
}
