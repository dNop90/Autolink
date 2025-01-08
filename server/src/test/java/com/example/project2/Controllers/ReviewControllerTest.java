package com.example.project2.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.example.project2.Entities.Review;
import com.example.project2.Entities.Vehicle;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.ReviewService;
import com.example.project2.Services.VehicleService;

import io.jsonwebtoken.Claims;

public class ReviewControllerTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReviewsByVehicleId_Success() {
        // Arrange
        Long vehicleId = 1L;
        Review review1 = new Review(); // Assume Review has a default constructor
        Review review2 = new Review();
        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewService.getReviewsByVehicleId(vehicleId)).thenReturn(reviews);

        // Act
        ResponseEntity<List<Review>> response = reviewController.getReviewsByVehicleId(vehicleId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testAddReview_Success() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long vehicleId = 1L;
        Review review = new Review();
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId); // Ensure you're using the correct setter

        Claims claims = mock(Claims.class);
        when(claims.getId()).thenReturn("123"); // Mocking getId() to return a valid String

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            utilities.when(() -> JWTUtil.parseToken(validJWT)).thenReturn(claims);
            when(vehicleService.getVehicleById(vehicleId)).thenReturn(ResponseEntity.ok(vehicle));

            // Mock the addReview method to return the review
            when(reviewService.addReview(any(Review.class), any(Long.class))).thenReturn(review);

            // Act
            ResponseEntity<Review> response = reviewController.addReview(validJWT, vehicleId, review);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(review, response.getBody());
        }
    }

    @Test
    public void testAddReview_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Long vehicleId = 1L;
        Review review = new Review();

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act & Assert
            try {
                reviewController.addReview(invalidJWT, vehicleId, review);
            } catch (ResponseStatusException e) {
                assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            }
        }
    }

    @Test
    public void testAddReview_VehicleNotFound() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long vehicleId = 1L;
        Review review = new Review();

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            utilities.when(() -> JWTUtil.parseToken(validJWT)).thenReturn(mock(Claims.class));
            when(vehicleService.getVehicleById(vehicleId)).thenReturn(ResponseEntity.notFound().build());

            // Act
            ResponseEntity<Review> response = reviewController.addReview(validJWT, vehicleId, review);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}