package com.cooksys.twitter_api.embeddables;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Credentials {
	
	private String username;
    private String password; 

}
