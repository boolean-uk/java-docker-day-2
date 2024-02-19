package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Profile;
import com.booleanuk.api.models.Account;
import com.booleanuk.api.repositories.ProfileRepository;
import com.booleanuk.api.repositories.AccountRepository;
import com.booleanuk.api.repositories.TweetRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.ProfileListResponse;
import com.booleanuk.api.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("profiles")

public class ProfileController {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TweetRepository tweetRepository;

    @GetMapping
    public ResponseEntity<ProfileListResponse> getAllProfiles() {
        ProfileListResponse profileListResponse = new ProfileListResponse();
        profileListResponse.set(this.profileRepository.findAll());
        return ResponseEntity.ok(profileListResponse);
    }

    @PostMapping("/account_id_{account_id}")
    public ResponseEntity<Response<?>> createProfile(@PathVariable int account_id, @RequestBody Profile profile) {
        ProfileResponse profileResponse = new ProfileResponse();
        if (!this.accountRepository.existsById(account_id)) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            profile.setTweets(new ArrayList<>());
            profile.setAccount(this.accountRepository.getReferenceById(account_id));
            profileResponse.set(this.profileRepository.save(profile));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(profileResponse, HttpStatus.CREATED);
    }

    @GetMapping("/account_id_{account_id}")
    public ResponseEntity<Response<?>> getProfilesForAccount(@PathVariable int account_id) {
        Account account = this.accountRepository.findById(account_id).orElse(null);
        if (account == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Profile> allSpecifiedProfiles = new ArrayList<>();
        for (Profile profile : this.profileRepository.findAll()) {
            if (profile.getAccount().getId() == account_id) {
                allSpecifiedProfiles.add(profile);
            }
        }
        ProfileListResponse profileListResponse = new ProfileListResponse();
        profileListResponse.set(allSpecifiedProfiles);
        return ResponseEntity.ok(profileListResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateProfile(@PathVariable int id, @RequestBody Profile profile) {
        Profile profileToUpdate = this.profileRepository.findById(id).orElse(null);
        if (profileToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        profileToUpdate.setName(profile.getName());
        profileToUpdate.setDescription(profile.getDescription());
        profileToUpdate.setFollowing(profile.getFollowing());
        profileToUpdate.setFollowers(profile.getFollowers());
        try {
            profileToUpdate = this.profileRepository.save(profileToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.set(profileToUpdate);
        return new ResponseEntity<>(profileResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteProfile(@PathVariable int id) {
        Profile profileToDelete = this.profileRepository.findById(id).orElse(null);
        if (profileToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (!profileToDelete.getTweets().isEmpty()) {
            this.tweetRepository.deleteAll();
        }
        this.profileRepository.delete(profileToDelete);
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.set(profileToDelete);
        return ResponseEntity.ok(profileResponse);
    }
}
