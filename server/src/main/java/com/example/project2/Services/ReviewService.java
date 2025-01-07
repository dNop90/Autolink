package com.example.project2.Services;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Review;
import com.example.project2.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AccountService accountService;

    public List<Review> getReviewsByVehicleId(Long vehicleId) {
        return reviewRepository.findByVehicle_VehicleId(vehicleId); // Ensure this method is correct
    }

    public Review addReview(Review review, Long reviewerId) {
        // Fetch the reviewer from the database
        Account reviewer = accountService.findById(reviewerId);
        if (reviewer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewer not found");
        }

        review.setReviewer(reviewer); // Set the reviewer for the review
        return reviewRepository.save(review); // Save the review
    }
}