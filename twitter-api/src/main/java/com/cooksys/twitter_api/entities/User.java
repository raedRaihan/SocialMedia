package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "user_table") // Custom table name to avoid SQL keyword conflicts.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false) // Automatically set when created, non-editable.
    private Timestamp joined;

    @Column(nullable = false)
    private boolean deleted; // Soft delete flag.

    @Embedded
    private Profile profile; // Embedded profile containing firstName, lastName, email, phone.

    @Embedded
    private Credentials credentials; // Embedded credentials containing username and password.

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> authoredTweets; // List of tweets authored by this user.

    @ManyToMany
    @JoinTable(
        name = "followers_following", // Join table for self-referencing many-to-many relationship.
        joinColumns = @JoinColumn(name = "follower_id"), // Current user as follower.
        inverseJoinColumns = @JoinColumn(name = "following_id") // Target user being followed.
    )
    private List<User> following; // List of users the current user is following.

    @ManyToMany(mappedBy = "following")
    private List<User> followers; // List of users following the current user.

    @ManyToMany
    @JoinTable(
        name = "user_likes", // Join table for user likes.
        joinColumns = @JoinColumn(name = "user_id"), // Current user liking a tweet.
        inverseJoinColumns = @JoinColumn(name = "tweet_id") // Liked tweet.
    )
    private List<Tweet> likedTweets; // List of tweets liked by the user.

    @ManyToMany
    @JoinTable(
        name = "user_mentions", // Join table for user mentions.
        joinColumns = @JoinColumn(name = "user_id"), // Mentioned user.
        inverseJoinColumns = @JoinColumn(name = "tweet_id") // Tweet mentioning the user.
    )
    private List<Tweet> mentionedTweets; // List of tweets mentioning this user.
}
