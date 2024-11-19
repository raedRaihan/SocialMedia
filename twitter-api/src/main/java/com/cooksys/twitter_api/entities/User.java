package com.cooksys.twitter_api.entities;

import java.security.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.cooksys.twitter_api.embeddables.Credentials;
import com.cooksys.twitter_api.embeddables.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
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
    private Credentials credential;
    

    @ManyToMany
    @JoinTable(
            name = "followers_following", 
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
        )
     private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;  
    
    

    @ManyToMany(mappedBy = "usersWhoLiked") 
	private List<Tweet> likedTweets;

    @ManyToMany(mappedBy = "mentionedUsers") 
	private List<Tweet> userMentioned;
}
