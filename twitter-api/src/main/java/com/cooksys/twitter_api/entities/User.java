package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users") // Custom table name to avoid conflicts with reserved SQL keywords.
public class User {

    @Id
    @GeneratedValue
	@Column(name = "user_id")
    private Long id;

    @CreationTimestamp
    private Timestamp joined;

    private boolean deleted;

    @Embedded
    private Profile profile;

    @Embedded
    private Credentials credentials; // Embedded Credentials

    @OneToMany(mappedBy = "author") // Maps to the 'author' field in the Tweet entity.
    private List<Tweet> authoredTweets; // List of tweets authored by this user.

    @ManyToMany
    @JoinTable(
        name = "followers_following", // Explicitly naming join table as per ERD.
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> following; // List of user objects that the current user instance is following

    @ManyToMany(mappedBy = "following")
    private List<User> followers; // Inverse side of self-referencing many-to-many relationship with user

    @ManyToMany
    @JoinTable(
        name = "user_likes", // Explicitly naming join table as per ERD.
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likedTweets; // List of tweet objects that a user has liked

    @ManyToMany
    @JoinTable(
        name = "user_mentions", // Explicitly naming join table as per ERD.
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> mentionedTweets; // List of tweet objects where a user is mentioned
}
