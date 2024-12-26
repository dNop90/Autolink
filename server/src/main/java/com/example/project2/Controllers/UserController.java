package com.example.project2.Controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.example.project2.models.DTOs.RegistrationUserDTO;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        Optional<Account> userOptional = accountService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.ok(userOptional.get());
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody RegistrationUserDTO updateUserDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        try {
            accountService.updateUserProfile(userId, updateUserDTO);
            return ResponseEntity.ok("User profile updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile.");
        }
    }
    
}
