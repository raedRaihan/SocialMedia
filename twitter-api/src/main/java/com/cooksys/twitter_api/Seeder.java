/*package com.cooksys.twitter_api;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.twitter_api.entities.Credentials;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Profile;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

    private final HashtagRepository hashtagRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        // --- User 1 ---
        // Credentials
        Credentials user1Cred = new Credentials();
        user1Cred.setUsername("therealmc");  // Set the username in Credentials
        user1Cred.setPassword("Password");

        User user1 = new User();
        user1.setCredentials(user1Cred);  // Set the Credentials object

        // Profile
        Profile user1Pro = new Profile();
        user1Pro.setFirstName("Master");
        user1Pro.setLastName("Chief");
        user1Pro.setEmail("sierra117@email.com");
        user1Pro.setPhone("123-456-7890");
        user1.setProfile(user1Pro);

        // Deleted
        user1.setDeleted(false);

        // --- User 2 ---
        // Credentials
        Credentials user2Cred = new Credentials();
        user2Cred.setUsername("mario");  // Set the username in Credentials
        user2Cred.setPassword("password");

        User user2 = new User();
        user2.setCredentials(user2Cred);  // Set the Credentials object

        // Profile
        Profile user2Pro = new Profile();
        user2Pro.setFirstName("Mario");
        user2Pro.setLastName("Mario");
        user2Pro.setEmail("mario@email.com");
        user2Pro.setPhone("234-567-8901");
        user2.setProfile(user2Pro);
        user2.setDeleted(false);

        // --- User 3 ---
        // Credentials
        Credentials user3Cred = new Credentials();
        user3Cred.setUsername("Luigi");  // Set the username in Credentials
        user3Cred.setPassword("Password");

        User user3 = new User();
        user3.setCredentials(user3Cred);  // Set the Credentials object

        // Profile
        Profile user3Pro = new Profile();
        user3Pro.setFirstName("Luigi");
        user3Pro.setLastName("Mario");
        user3Pro.setEmail("luigi@email.com");
        user3Pro.setPhone("345-678-9012");
        user3.setProfile(user3Pro);
        user3.setDeleted(false);

        // --- User 4 ---
        // Credentials
        Credentials user4Cred = new Credentials();
        user4Cred.setUsername("Nathan");  // Set the username in Credentials
        user4Cred.setPassword("Password");

        User user4 = new User();
        user4.setCredentials(user4Cred);  // Set the Credentials object

        // Profile
        Profile user4Pro = new Profile();
        user4Pro.setFirstName("Nathan");
        user4Pro.setLastName("Drake");
        user4Pro.setEmail("nathan@email.com");
        user4Pro.setPhone("456-789-0023");
        user4.setProfile(user4Pro);
        user4.setDeleted(false);

        // --- User 5 ---
        // Credentials
        Credentials user5Cred = new Credentials();
        user5Cred.setUsername("Tarnished");  // Set the username in Credentials
        user5Cred.setPassword("Password");

        User user5 = new User();
        user5.setCredentials(user5Cred);  // Set the Credentials object

        // Profile
        Profile user5Pro = new Profile();
        user5Pro.setFirstName("The");
        user5Pro.setLastName("Tarnished");
        user5Pro.setEmail("willibecometheeldenlord@email.com");
        user5Pro.setPhone("567-890-0034");
        user5.setProfile(user5Pro);
        user5.setDeleted(false);

        // --- User 6 ---
        // Credentials
        Credentials deletedUserCred = new Credentials();
        deletedUserCred.setUsername("DeletedUser");  // Set the username in Credentials
        deletedUserCred.setPassword("Password");

        User deletedUser = new User();
        deletedUser.setCredentials(deletedUserCred);  // Set the Credentials object

        // Profile
        Profile deletedUserPro = new Profile();
        deletedUserPro.setFirstName("Deleted");
        deletedUserPro.setLastName("User");
        deletedUserPro.setEmail("Deleted@User.com");
        deletedUserPro.setPhone("NULL");
        deletedUser.setProfile(deletedUserPro);
        deletedUser.setDeleted(true);

        // Save all users
        userRepository.saveAllAndFlush(Arrays.asList(user1, user2, user3, user4, user5, deletedUser));

        // ----- HASHTAGS -----

        Hashtag hashtag1 = new Hashtag();
        hashtag1.setLabel("#eldenlord");

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setLabel("#mario");

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setLabel("#luigi");

        Hashtag hashtag4 = new Hashtag();
        hashtag4.setLabel("#whereiscortana");

        hashtagRepository.saveAllAndFlush(Arrays.asList(hashtag1, hashtag2, hashtag3, hashtag4));

        // ----- TWEETS -----
        // --- Start Tweet 1 ---
        Tweet tweet1 = new Tweet();
        tweet1.setAuthor(user1);
        tweet1.setDeleted(false);
        tweet1.setContent("This is some content 1 tweet1 #eldenlord #mario");
        tweet1.setHashtags(Arrays.asList(hashtag1, hashtag2));
        tweet1.setMentionedUsers(Arrays.asList(user1, user2));
        tweetRepository.saveAndFlush(tweet1);

        // --- Start Tweet 2 ---
        Tweet tweet2 = new Tweet();
        tweet2.setAuthor(user1);
        tweet2.setDeleted(false);
        tweet2.setContent("This is some content 2 tweet2 #eldenlord #mario");
        tweet2.setHashtags(Arrays.asList(hashtag1, hashtag2));
        tweet2.setInReplyTo(tweet1);
        tweetRepository.saveAndFlush(tweet2);

        // --- Start Tweet 3 ---
        Tweet tweet3 = new Tweet();
        tweet3.setAuthor(user2);
        tweet3.setDeleted(false);
        tweet3.setContent("This is some content 3 tweet3 #luigi #whereiscortana");
        tweet3.setHashtags(Arrays.asList(hashtag3, hashtag4));
        tweet3.setInReplyTo(tweet2);
        tweetRepository.saveAndFlush(tweet3);

        // --- Start Tweet 4 ---
        Tweet tweet4 = new Tweet();
        tweet4.setAuthor(user2);
        tweet4.setDeleted(false);
        tweet4.setContent("This is some content 4 tweet4");
        tweet4.setInReplyTo(tweet3);
        tweetRepository.saveAndFlush(tweet4);

        // --- Start Tweet 5 ---
        Tweet tweet5 = new Tweet();
        tweet5.setAuthor(user3);
        tweet5.setDeleted(false);
        tweet5.setContent("This is some content 5 tweet5");
        tweet5.setMentionedUsers(Arrays.asList(user1, user2));
        tweet5.setInReplyTo(tweet4);
        tweetRepository.saveAndFlush(tweet5);

        // --- Start Tweet 6 ---
        Tweet tweet6 = new Tweet();
        tweet6.setAuthor(user3);
        tweet6.setDeleted(false);
        tweet6.setRepostOf(tweet5);
        tweet6.setMentionedUsers(Arrays.asList(user1, user2));
        tweet6.setInReplyTo(tweet2);
        tweetRepository.saveAndFlush(tweet6);

        // --- Start Tweet 7 ---
        Tweet deletedTweet = new Tweet();
        deletedTweet.setAuthor(user3);
        deletedTweet.setDeleted(true);
        deletedTweet.setContent("This is a deleted tweet (User3) tweet7");
        deletedTweet.setMentionedUsers(Arrays.asList(user1, user2));
        tweetRepository.saveAndFlush(deletedTweet);

        // ----- LIST of Tweets + Adding to User# -----
        List<Tweet> user1Tweets = List.of(tweet1, tweet2);
        user1.setAuthoredTweets(user1Tweets); // Changed to setAuthoredTweets()
        userRepository.saveAndFlush(user1);

        List<Tweet> user2Tweets = List.of(tweet3, tweet4);
        user2.setAuthoredTweets(user2Tweets); // Changed to setAuthoredTweets()
        userRepository.saveAndFlush(user2);

        List<Tweet> user3Tweets = List.of(tweet5, tweet6);
        user3.setAuthoredTweets(user3Tweets); // Changed to setAuthoredTweets()
        userRepository.saveAndFlush(user3);

        // ----- List of Liked Tweets -----
        user1.setLikedTweets(user3Tweets);
        userRepository.saveAndFlush(user1);

        user2.setLikedTweets(user1Tweets);
        user2.setLikedTweets(user2Tweets);
        userRepository.saveAndFlush(user2);

        user3.setLikedTweets(user2Tweets);
        userRepository.saveAndFlush(user3);

        deletedUser.setLikedTweets(user2Tweets);
        userRepository.saveAndFlush(deletedUser);

        // ----- List of Following -----
        List<User> followingList = List.of(user2, user3, user4);
        user1.setFollowing(followingList);
        userRepository.saveAndFlush(user1);

        // ----- List of Followers -----
        List<User> followersList = List.of(user3, user5);
        user1.setFollowers(followersList);
        userRepository.saveAndFlush(user1);

        // ----- Tweet Mentions -----
        Tweet mention1 = new Tweet();
        mention1.setAuthor(user2);
        mention1.setDeleted(false);
        mention1.setContent("This is some content for tweet mention 1");
        tweetRepository.saveAndFlush(mention1);

        // Following
        List<User> following_1 = List.of(user2, user3, user4, deletedUser);
        user1.setFollowing(following_1);

        List<User> followers_1 = List.of(user5, deletedUser);
        user1.setFollowers(followers_1);
        userRepository.saveAndFlush(user1);
    }

}*/
