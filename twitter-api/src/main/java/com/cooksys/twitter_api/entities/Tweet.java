package com.cooksys.twitter_api.entities;

import java.security.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    private Long id;
    
    // Foreign key
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "user_id", nullable = false)
    private User author;
    
    private Timestamp timestamp;
    
    private boolean deleted;
    
    private String content;
    
    // Foreign key
    @ManyToOne
    @JoinColumn(name = "inReplyTo")
    private Tweet inReplyTo;
    
    // Foreign key
    @ManyToOne
    @JoinColumn(name = "repostOf")
    private Tweet repostOf;
    
    @ManyToMany
    @JoinTable(
        name = "user_likes", 
        joinColumns = @JoinColumn(name = "tweet_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> usersWhoLiked;
    
    @ManyToMany
    @JoinTable(
        name = "user_mentions", 
        joinColumns = @JoinColumn(name = "tweet_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> mentionedUsers;
    
    // New Many-to-Many relationship for hashtags
    @ManyToMany
    @JoinTable(
        name = "tweet_hashtags", // Make sure this matches the join table used in the Hashtag entity
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags; // List of hashtags associated with the tweet
}
