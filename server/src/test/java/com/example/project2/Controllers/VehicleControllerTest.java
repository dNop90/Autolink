package com.example.project2.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.example.project2.Entities.Vehicle;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.ReviewService;
import com.example.project2.Services.VehicleService;

import io.jsonwebtoken.Claims;

public class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private VehicleController vehicleController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVehicles() {
        // Arrange
        Vehicle vehicle1 = new Vehicle(); // Assume Vehicle has a default constructor
        Vehicle vehicle2 = new Vehicle();
        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2);
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleController.getAllVehicles();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetVehicleById_Success() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(ResponseEntity.ok(vehicle));

        // Act
        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(vehicleId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateVehicle_Success() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Vehicle vehicle = new Vehicle();
        Long dealerId = 1L;

        Claims claims = Mockito.mock(Claims.class);
        when(claims.getId()).thenReturn("someId");

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            utilities.when(() -> JWTUtil.parseToken(validJWT)).thenReturn(claims);
            when(vehicleService.createVehicle(any(Vehicle.class), any(Long.class))).thenReturn(vehicle);

            // Act
            Vehicle result = vehicleController.createVehicle(validJWT, vehicle, dealerId);

            // Assert
            assertEquals(vehicle, result);
        }
    }

    @Test
    public void testCreateVehicle_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Vehicle vehicle = new Vehicle();
        Long dealerId = 1L;

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            // Mock the static method to return false for isValid
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);
            // Mock parseToken to return null or throw an exception
            utilities.when(() -> JWTUtil.parseToken(invalidJWT)).thenReturn(null);

            // Act & Assert
            try {
                vehicleController.createVehicle(invalidJWT, vehicle, dealerId);
            } catch (ResponseStatusException e) {
                assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            }
        }
    }

    @Test
    public void testUpdateVehicle_Success() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(vehicleService.updateVehicle(any(Long.class), any(Vehicle.class)))
                    .thenReturn(ResponseEntity.ok(vehicle));

            // Act
            ResponseEntity<ResponseEntity<Vehicle>> response = vehicleController.updateVehicle(validJWT, vehicleId,
                    vehicle);

            // Assert
            assertEquals(HttpStatus.OK, response.getBody().getStatusCode());
        }
    }

    @Test
    public void testDeleteVehicle_Success() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long vehicleId = 1L;

        Claims claims = Mockito.mock(Claims.class);
        when(claims.getId()).thenReturn("someId");

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            // Mock the static methods
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            utilities.when(() -> JWTUtil.parseToken(validJWT)).thenReturn(claims);

            // Mock the vehicleService.deleteVehicle method
            // Assuming deleteVehicle returns ResponseEntity<Void>
            when(vehicleService.deleteVehicle(vehicleId)).thenReturn(ResponseEntity.noContent().build());

            // Act
            ResponseEntity<Void> response = vehicleController.deleteVehicle(validJWT, vehicleId);

            // Assert
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Test
    public void testDeleteVehicle_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Long vehicleId = 1L;

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            // Mock the static method to return false
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act & Assert
            try {
                vehicleController.deleteVehicle(invalidJWT, vehicleId);
            } catch (ResponseStatusException e) {
                assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            }
        }
    }

    @Test
    public void testGetBuyerIdsByAccountId_Success() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long accountId = 1L;
        List<Long> buyerIds = Arrays.asList(1L, 2L, 3L);

        Claims claims = Mockito.mock(Claims.class);
        when(claims.getId()).thenReturn("someId");

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            // Mock the static methods
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            utilities.when(() -> JWTUtil.parseToken(validJWT)).thenReturn(claims);
            when(vehicleService.getBuyerIdsByAccountId(accountId)).thenReturn(buyerIds);

            // Act
            List<Long> result = vehicleController.getBuyerIdsByAccountId(validJWT, accountId);

            // Assert
            assertEquals(3, result.size());
        }
    }

    @Test
    public void testGetBuyerIdsByAccountId_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Long accountId = 1L;

        try (MockedStatic<JWTUtil> utilities = mockStatic(JWTUtil.class)) {
            // Mock the static method to return false
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act & Assert
            try {
                vehicleController.getBuyerIdsByAccountId(invalidJWT, accountId);
            } catch (ResponseStatusException e) {
                assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            }
        }
    }
}