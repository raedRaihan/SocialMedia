package com.cooksys.twitter_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/username/exists/@{username}")
    public boolean doesUsernameExist(@PathVariable String username) {
        return validateService.usernameExists(username);
    }

    @GetMapping("/username/available/@{username}")
    public boolean isUsernameAvailable(@PathVariable String username) {
        return validateService.isUsernameAvailable(username);
    }

    @GetMapping("/tag/exists/{label}")
    public boolean doesTagExist(@PathVariable String label) {
        return validateService.doesTagExist(label);
    }
}
