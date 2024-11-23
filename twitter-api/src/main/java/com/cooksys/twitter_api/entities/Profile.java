package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true) // Ensure username is unique and non-null.
    private String email;

    @Column(nullable = false) // Password must be provided.
    private String phone;
}