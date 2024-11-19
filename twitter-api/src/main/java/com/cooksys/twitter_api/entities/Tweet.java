package com.cooksys.twitter_api.entities;

import java.security.Timestamp;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
	@GeneratedValue
	@Column(name = "tweet_id")
	private Long id;
	
	//foreign key
	@ManyToOne
	@JoinColumn(name="author",referencedColumnName = "user_id", nullable = false)
	private User author;
	
	private Timestamp timestamp;
	
	private boolean deleted;
	
	private String content;
	
	//foreign key
	@ManyToOne
	@JoinColumn(name = "inReplyTo", referencedColumnName = "tweet_id")
	private Tweet inReplyTo;
	
	//foreign key
	@ManyToOne
	@JoinColumn(name = "repostOf", referencedColumnName = "tweet_id")
	private Tweet repostOf;
	
	@ManyToMany
	@JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> usersWhoLiked;
	
	@ManyToMany
	@JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> mentionedUsers;
	

}
