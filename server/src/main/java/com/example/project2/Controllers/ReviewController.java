package com.example.project2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import com.example.project2.Entities.Review;
import com.example.project2.Entities.Vehicle;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.ReviewService;
import com.example.project2.Services.VehicleService;

@RestController
@CrossOrigin
@RequestMapping("/api/vehicles")
public class ReviewController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ReviewService reviewService;

    /*
     * Get reviews for a specific vehicle
     */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsByVehicleId(@PathVariable Long id) {
        List<Review> reviews = reviewService.getReviewsByVehicleId(id);
        return ResponseEntity.ok(reviews);
    }

    /*
     * Add a review for a specific vehicle
     */
    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> addReview(
            @RequestHeader("Authorization") String authHeader, // Include the authorization header
            @PathVariable Long id,
            @RequestBody Review review) {

        // Validate the authorization header
        if (!JWTUtil.isValid(authHeader)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

        Vehicle vehicle = vehicleService.getVehicleById(id).getBody();

        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        review.setVehicle(vehicle); // Set the vehicle for the review

        // Get the current user from the JWT token
        Long reviewerId = Long.valueOf(JWTUtil.parseToken(authHeader).getId()); // Convert to Long
        reviewService.addReview(review, reviewerId); // Pass both review and reviewerId

        return ResponseEntity.status(HttpStatus.CREATED).body(review); // Return the saved review
    }

}