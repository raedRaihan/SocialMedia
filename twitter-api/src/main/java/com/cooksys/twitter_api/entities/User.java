package com.cooksys.twitter_api.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class User {
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;
	
	
	@ManyToMany(mappedBy = "usersWhoLiked") 
	private List<Tweet> likedTweets;
	
	@ManyToMany(mappedBy = "mentionedUsers") 
	private List<Tweet> userMentioned;
	
	
	

}
