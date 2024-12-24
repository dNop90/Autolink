package com.example.project2.Controllers;

import java.security.NoSuchAlgorithmException;

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
    
    /*
     * Register endpoint
     * Calls register service
     * @param account, valid account with fields username, email, and password
     * @return a ResponseEntity with the account response or corresponding error message
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account) {
        System.out.println(account);
        try {
            AccountResponse res = accountService.register(account);
            return ResponseEntity.status(200).body(res);
        }catch (DuplicateUsernameException d) {
            return ResponseEntity.status(409).body("Username taken");
        }catch (DuplicateEmailException d) {
            return ResponseEntity.status(409).body("Email already registered");
        }catch (NoSuchAlgorithmException d) {
            return ResponseEntity.status(500).body("Hashing algorithm not fount");
        }
    }

    /*
     * Register endpoint
     * Calls login service
     * @param account, valid account with fields username or email, and password
     * @return a ResponseEntity with the account response or corresponding error message
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        try {
             AccountResponse res = accountService.login(account);
             return ResponseEntity.status(200).body(res);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(401).body("No account with username");
        } catch (PasswordIncorrectException e) {
            return ResponseEntity.status(401).body("Invalid password");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(500).body("Hashing algorithm not fount");
        }
    }
}
