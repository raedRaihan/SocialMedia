package com.cooksys.twitter_api.services;

public interface ValidateService {
    
        /* GET /validate/username/exists/@{username} */
        boolean usernameExists(String username);

        /* GET /validate/username/available/@{username} */
        boolean isUsernameAvailable(String username);
}
