package com.example.project2.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Review;
import com.example.project2.Repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReviewsByVehicleId_Success() {
        // Arrange
        Long vehicleId = 1L;
        Review review1 = new Review();
        Review review2 = new Review();
        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findByVehicle_VehicleId(vehicleId)).thenReturn(reviews);

        // Act
        List<Review> result = reviewService.getReviewsByVehicleId(vehicleId);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testAddReview_Success() {
        // Arrange
        Review review = new Review();
        Long reviewerId = 1L;
        Account reviewer = new Account();
        reviewer.setAccountId(reviewerId);

        when(accountService.findById(reviewerId)).thenReturn(reviewer);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        Review result = reviewService.addReview(review, reviewerId);

        // Assert
        assertNotNull(result);
        assertEquals(review, result);
        assertEquals(reviewer, result.getReviewer());
    }

    @Test
    public void testAddReview_ReviewerNotFound() {
        // Arrange
        Review review = new Review();
        Long reviewerId = 1L;

        when(accountService.findById(reviewerId)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.addReview(review, reviewerId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Reviewer not found", exception.getReason());
    }
}