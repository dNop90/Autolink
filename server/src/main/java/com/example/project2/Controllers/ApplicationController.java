package com.example.project2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.Entities.Application;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.ApplicationService;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("api/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    /*
     * Application endpoint for users to apply as dealer
     * 
     * @param JWT token for authorization, application object with required fields
     * 
     * @return a ResponseEntity with success or corresponding error message
     */
    @PostMapping("/apply")
    public ResponseEntity apply(@RequestHeader("Authorization") String authHeader,
            @RequestBody Application application) {
        if (JWTUtil.isValid(authHeader)) {
            String res = applicationService.apply(application);
            return ResponseEntity.status(200).body(res);
        }
        return ResponseEntity.status(401).build();

    }

    /*
     * Application endpoint for admins to see all active applications
     * 
     * @param JWT token for authorization
     * 
     * @return a ResponseEntity with the list of applications
     * or corresponding error mesage
     */
    @GetMapping("/applications")
    public ResponseEntity getDealerApplications(@RequestHeader("Authorization") String authHeader) {
        if (JWTUtil.isValid(authHeader)) {
            return ResponseEntity.status(200).body(applicationService.getPendingApplications());
        }
        return ResponseEntity.status(401).build();
    }

    /*
     * Application endpoint for admins to see all applications and their status
     * 
     * @param JWT token for authorization
     * 
     * @return a ResponseEntity with the list of applications
     * or corresponding error mesage
     */
    @GetMapping("/application/{accountId}")
    public ResponseEntity getDealerApplication(@RequestHeader("Authorization") String authHeader, @PathVariable Integer accountId) {
        if(JWTUtil.isValid(authHeader)) {
            return ResponseEntity.status(200).body(applicationService.geApplicationByAccountId(accountId));
        }
        return ResponseEntity.status(401).build();
    }

    /*
     * Application endpoint for user to see status applications
     * 
     * @param JWT token for authorization
     * 
     * @return a ResponseEntity with the status of the application
     * or corresponding error mesage
     */
    @PatchMapping("/approve/{applicationId}/{status}")
    public ResponseEntity approveApplication(@RequestHeader("Authorization") String authHeader,
            @PathVariable Long applicationId, @PathVariable String status) {
        if (JWTUtil.isValid(authHeader)) {
            try {
                return ResponseEntity.status(200).body(applicationService.approve(applicationId, status));
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).body("User not found.");
            }
        }
        return ResponseEntity.status(401).build();
    }
}
