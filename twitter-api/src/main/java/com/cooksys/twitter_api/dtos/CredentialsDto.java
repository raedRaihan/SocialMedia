package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CredentialsDto {
    private String username;
    private String password; // for validating credentials in requests

    /*public Credentials requestDtoToEntity(CredentialsDto credentials) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/
}
