package com.example.project2.Services;

import com.example.project2.Entities.Review;
import com.example.project2.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByVehicleId(Long vehicleId) {
        return reviewRepository.findByVehicle_VehicleId(vehicleId); // Ensure this method is correct
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}