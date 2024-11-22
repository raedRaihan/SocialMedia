package com.cooksys.twitter_api.entities;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
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
    private User author;
    
    @CreationTimestamp
    private Timestamp timestamp;
    
    private boolean deleted;
    
    private String content;
    
    // Foreign key
    @ManyToOne
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies = new ArrayList<>();
    
    // Foreign key
    @ManyToOne
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts = new ArrayList<>();
    
    @ManyToMany
    private List<User> usersWhoLiked;
    
    @ManyToMany
    private List<User> mentionedUsers;
    
    // New Many-to-Many relationship for hashtags
    @ManyToMany
    private List<Hashtag> hashtags; // List of hashtags associated with the tweet
}
