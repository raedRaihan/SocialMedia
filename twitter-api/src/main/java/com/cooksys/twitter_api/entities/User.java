package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "user_table") // Custom table name to avoid conflicts with reserved SQL keywords.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private List<User> following; // List of user objects that the current user instance is following

    @ManyToMany(mappedBy = "following")
    private List<User> followers; // Inverse side of self-referencing many-to-many relationship with user

    @ManyToMany
    private List<Tweet> likedTweets; // List of tweet objects that a user has liked

    @ManyToMany
    private List<Tweet> mentionedTweets; // List of tweet objects where a user is mentioned
}
