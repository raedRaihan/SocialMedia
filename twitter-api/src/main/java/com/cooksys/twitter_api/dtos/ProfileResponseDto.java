package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileResponseDto {
    private String firstName;
    private String lastName;
    private String email; // required
    private String phone;
}
