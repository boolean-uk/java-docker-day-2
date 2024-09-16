package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Profile;
import com.booleanuk.api.models.Account;
import com.booleanuk.api.repositories.ProfileRepository;
import com.booleanuk.api.repositories.AccountRepository;
import com.booleanuk.api.repositories.TweetRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.AccountListResponse;
import com.booleanuk.api.responses.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private TweetRepository tweetRepository;

    @GetMapping
    public ResponseEntity<AccountListResponse> getAllAccounts() {
        AccountListResponse accountListResponse = new AccountListResponse();
        accountListResponse.set(this.accountRepository.findAll());
        return ResponseEntity.ok(accountListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createAccount(@RequestBody Account account) {
        AccountResponse accountResponse = new AccountResponse();
        try {
            accountResponse.set(this.accountRepository.save(account));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAccountById(@PathVariable int id) {
        Account account = this.accountRepository.findById(id).orElse(null);
        if (account == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.set(account);
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateAccount(@PathVariable int id, @RequestBody Account account) {
        Account accountToUpdate = this.accountRepository.findById(id).orElse(null);
        if (accountToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        accountToUpdate.setAccountName(account.getAccountName());
        accountToUpdate.setEmail(account.getEmail());
        accountToUpdate.setPassword(account.getPassword());
        try {
            accountToUpdate = this.accountRepository.save(accountToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.set(accountToUpdate);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteAccount(@PathVariable int id) {
        Account accountToDelete = this.accountRepository.findById(id).orElse(null);
        if (accountToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (accountToDelete.getProfile() != null) {
            if (!accountToDelete.getProfile().getTweets().isEmpty()) {
                this.tweetRepository.deleteAll();
            }
            this.profileRepository.delete(accountToDelete.getProfile());
        }
        this.accountRepository.delete(accountToDelete);
        accountToDelete.setProfile(new Profile());
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.set(accountToDelete);
        return ResponseEntity.ok(accountResponse);
    }
}
