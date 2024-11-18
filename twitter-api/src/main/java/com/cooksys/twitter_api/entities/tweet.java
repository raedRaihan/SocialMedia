package com.cooksys.twitter_api.entities;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class tweet {

	@Id
	@GeneratedValue
	private Long id;
	
	//foreign key
	@ManyToOne
	@JoinColumn(name="author",nullable = false)
	private int author;
	
	private String timestamp;
	
	private boolean deleted;
	
	private String content;
	
	//foreign key
	@ManyToOne
	@JoinColumn(name = "inReplyTo", referencedColumnName = "id")
	private int inReplyTo;
	
	//foreign key
	private int repostOf;
	

}
