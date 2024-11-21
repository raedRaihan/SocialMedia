package com.cooksys.twitter_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.entities.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
        // Find a hashtag by its label (case-insensitive, optional for safety)
        Hashtag findByLabel(String label);

}

