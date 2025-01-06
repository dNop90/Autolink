package com.example.project2.Controllers;


import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.example.project2.Response.ProfileResponse;
import com.example.project2.Services.AccountService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserController {
    private final AccountService accountService;

    /*
     * Register endpoint
     * Calls register service
     * 
     * @param account, valid account with fields username, email, and password
     * 
     * @return a ResponseEntity with the success or corresponding error
     * message
     */

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account) {
        System.out.println(account);
        try {
            String res = accountService.register(account);
            return ResponseEntity.status(200).body(res);
        } catch (DuplicateUsernameException d) {
            return ResponseEntity.status(409).body("Username taken");
        } catch (DuplicateEmailException d) {
            return ResponseEntity.status(409).body("Email already registered");
        } catch (NoSuchAlgorithmException d) {
            return ResponseEntity.status(500).body("Hashing algorithm not found");
        }
    }

    /*
     * Login endpoint
     * Calls login service
     * 
     * @param account, valid account with fields username or email, and password
     * 
     * @return a ResponseEntity with the account response or corresponding error
     * message
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
            return ResponseEntity.status(500).body("Hashing algorithm not found");
        }
    }


    @GetMapping("/all")
    public List<Account> getAllUsers() {
        return accountService.getAllAccounts();
    }

    /*
     * Retrieve user profile information endpoind
     * 
     * @param JWT token for authorization and username of the account
     * 
     * @return a response entity with the profile object in body or corresponding
     * error message
     */

    @GetMapping("/info/{username}")
    public ResponseEntity getUserProfile(@RequestHeader("Authorization") String authHeader,
            @PathVariable String username) {
        if (JWTUtil.isValid(authHeader)) {
            try {
                ProfileResponse profile = accountService.getUserProfileByUsername(username);
                return ResponseEntity.ok(profile);
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).body("User not found");
            }
        }
        return ResponseEntity.status(401).build();
    }

    /*
     * Change Password
     * Validates current password and updates the new password
     * 
     * @param account, contains current password, new password, and username
     * 
     * @return a ResponseEntity with status of the password change operation
     */
    @PatchMapping("/password")
    public ResponseEntity changePassword(@RequestHeader("Authorization") String authHeader,
            @RequestBody Account account) {
        if (JWTUtil.isValid(authHeader)) {
            try {
                accountService.updatePassword(account);
                return ResponseEntity.status(200).body("Password updated");
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).body("User not found");
            } catch (PasswordIncorrectException e) {
                return ResponseEntity.status(401).body("Invalid password");
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.status(500).body("Hashing algorithm not found");
            }
        }
        return ResponseEntity.status(401).build();

    }

    /*
     * Update user profile endpoint
     * 
     * @param JWT token for authroization and username to search
     * 
     * @return a ResponseEntity with the corresponding message
     */
    @PatchMapping("/profile")
    public ResponseEntity updateUserProfile(@RequestHeader("Authorization") String authHeader,
            @RequestBody ProfileResponse profile) {
        if (JWTUtil.isValid(authHeader)) {
            try {
                accountService.updateUserProfile(profile);
                return ResponseEntity.status(200).body("Account updated");
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).body("User not found");
            }
        }
        return ResponseEntity.status(401).build();
    }

    /*
     * Search endpoint for admins looking for a specific account via username
     * 
     * @param JWT token for authorization, and username to search
     * 
     * @return a ResponseEntity with the account response or corresponding error
     * mesage
     */
    @PostMapping("/search")
    public ResponseEntity getUser(@RequestHeader("Authorization") String authHeader, @RequestBody String username) {
        if (JWTUtil.isValid(authHeader)) {
            try {
                AccountResponse user = accountService.getUserByUsername(username);
                return ResponseEntity.ok(user);
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }
        return ResponseEntity.status(401).build();
    }

    /*
     * Promote endpoint for admins to set a user account as admin
     * 
     * @param JWT token for authorization, and username to search for account
     * 
     * @return a ResponseEntity with the corresponding message
     */
    @PatchMapping("/promote")
    public ResponseEntity promote(@RequestHeader("Authorization") String authHeader, @RequestBody String username) {
        try {
            return ResponseEntity.status(200).body(accountService.promote(username));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /*
     * Promote endpoint for admins to set a user account as admin
     * 
     * @param JWT token for authorization, and username to search for account
     * 
     * @return a ResponseEntity with the corresponding message
     */
    @PatchMapping("/suspend/{status}")
    public ResponseEntity suspend(@RequestHeader("Authorization") String authHeader, @PathVariable Boolean status,
            @RequestBody String username) {

        if (JWTUtil.isValid(authHeader)) {
            try {
                return ResponseEntity.status(200).body(accountService.suspend(username, status));
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).body("User not found");
            }
        }
        return ResponseEntity.status(401).build();
    }

    /**
     * Check if the user token is valid or not
     * 
     * @param authHeader The token in the Authorization header
     * @return user information
     */
    @PostMapping("/token")
    public ResponseEntity<?> verifyUserToken(@RequestHeader("Authorization") String authHeader) {
        // Check if token is valid
        if (JWTUtil.isValid(authHeader)) {
            
            AccountResponse res = accountService.getCurrentUser(JWTUtil.parseToken(authHeader).getSubject(),
                    authHeader);

            return ResponseEntity.status(200).body(res);
        }

        // Return 401 if its NOT valid
        return ResponseEntity.status(401).build();
    }

}
