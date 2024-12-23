package com.example.project2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.Response.AccountResponse;
import com.example.project2.Services.AccountService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account) {
        try {
            AccountResponse res = accountService.register(account);
            return ResponseEntity.status(200).body(res);
        }catch (DuplicateUsernameException d) {
            return ResponseEntity.status(409).body("Username taken");
        }catch (DuplicateEmailException d) {
            return ResponseEntity.status(409).body("Email already registered");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        try {
             AccountResponse res = accountService.login(account);
             return ResponseEntity.status(200).body(res);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(401).body("Unauthorized");
        } catch (PasswordIncorrectException e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
