package com.example.project2.Controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Response.AccountResponse;
import com.example.project2.Services.AccountService;
import com.example.project2.models.DTOs.RegistrationUserDTO;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
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
            String res = accountService.register(account);
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
     * Login endpoint
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

    /**
     * Check if the user token is valid or not
     * @param authHeader The token in the Authorization header
     * @return user information
     */
    @PostMapping("/token")
    public ResponseEntity<?> verifyUserToken(@RequestHeader("Authorization") String authHeader) {
        //Check if token is valid
        if(JWTUtil.isValid(authHeader))
        {
            AccountResponse res = accountService.getCurrentUser(JWTUtil.parseToken(authHeader).getSubject(), authHeader);

            return ResponseEntity.status(200).body(res);
        }
        
        //Return 401 if its NOT valid
        return ResponseEntity.status(401).build();
    }
    


    /*
     * This is for testing the JWT
     */
    @GetMapping("/test")
    public String getTest(String test) {
        return JWTUtil.generateToken(test, "123");
    }

    @PostMapping("/test2")
    public String getTest3(@RequestHeader("Authorization") String authHeader, String test) {
        //Not valid
        if(!JWTUtil.isValid(authHeader))
        {
            return "ERROR token";
        }

        Claims claims = JWTUtil.parseToken(test);
        return claims.getSubject();
    }
}
