package com.cooksys.twitter_api.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

        private final HashtagRepository hashtagRepository;
        private final HashtagMapper hashtagMapper;
        private final TweetRepository tweetRepository;
        private final TweetMapper tweetMapper;

        @Override
        public List<HashtagDto> getAllHashtags() {
            return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
        }

        // Fetch all tweets associated with a specific hashtag label
        @Override
        public HashtagDto getTweetsByHashtagLabel(String label) {
            // Find the hashtag by its label in the database
            Hashtag hashtag = hashtagRepository.findByLabel(label);
            if (hashtag == null) {
                // If the hashtag is not found, throw an exception
                throw new NotFoundException("Hashtag with label '" + label + "' not found");
            }

            // Find all tweets associated with the hashtag, ordered by the most recent first
            List<Tweet> tweets = tweetRepository.findAllByHashtagAndDeletedFalseOrderByTimestampDesc(hashtag);

            // Map the list of Tweet entities to TweetResponseDto objects
            List<TweetResponseDto> tweetDtos = tweetMapper.entitiesToDtos(tweets);

            // Map the hashtag entity to a HashtagDto and set its associated tweets
            HashtagDto hashtagDto = hashtagMapper.entityToDto(hashtag);
            hashtagDto.setTweets(tweetDtos);

            // Return the final DTO
            return hashtagDto;
        }
}
