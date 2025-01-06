package com.example.project2.Controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.project2.Entities.Application;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.ApplicationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;

public class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for "apply" endpoint with valid JWT
     */
    @Test
    public void testApply_ValidJWT() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Application application = new Application();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT))
                    .thenReturn(true);
            when(applicationService.apply(application)).thenReturn("Application submitted successfully");

            // Act
            ResponseEntity response = applicationController.apply(validJWT, application);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Application submitted successfully", response.getBody());
        }
    }

    @Test
    public void testApply_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Application application = new Application();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act
            ResponseEntity response = applicationController.apply(invalidJWT, application);

            // Assert
            assertEquals(401, response.getStatusCodeValue());
        }
    }

    /**
     * Test for "getDealerApplications" endpoint with valid JWT
     */
    @Test
    public void testGetDealerApplications_ValidJWT() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        List<Application> applications = Arrays.asList(new Application(), new Application());
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(applicationService.getPendingApplications()).thenReturn(applications);

            // Act
            ResponseEntity response = applicationController.getDealerApplications(validJWT);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(applications, response.getBody());
        }
    }

    @Test
    public void testGetDealerApplications_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act
            ResponseEntity response = applicationController.getDealerApplications(invalidJWT);

            // Assert
            assertEquals(401, response.getStatusCodeValue());
        }
    }

    /**
     * Test for "getDealerApplication" endpoint
     */
    @Test
    public void testGetDealerApplication_ValidJWT() {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Integer accountId = 1;
        List<Application> applications = Arrays.asList(new Application());
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(applicationService.geApplicationByAccountId(accountId)).thenReturn(applications);

            // Act
            ResponseEntity response = applicationController.getDealerApplication(validJWT, accountId);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(applications, response.getBody());
        }
    }

    @Test
    public void testGetDealerApplication_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Integer accountId = 1;
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act
            ResponseEntity response = applicationController.getDealerApplication(invalidJWT, accountId);

            // Assert
            assertEquals(401, response.getStatusCodeValue());
        }
    }

    /**
     * Test for "approveApplication" endpoint
     */
    @Test
    public void testApproveApplication_ValidJWT() throws AccountNotFoundException {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long applicationId = 1L;
        String status = "Approved";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(applicationService.approve(applicationId, status)).thenReturn(status);

            // Act
            ResponseEntity response = applicationController.approveApplication(validJWT, applicationId, status);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(status, response.getBody());
        }
    }

    @Test
    public void testApproveApplication_AccountNotFound() throws AccountNotFoundException {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        Long applicationId = 1L;
        String status = "Approved";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(applicationService.approve(applicationId, status)).thenThrow(new AccountNotFoundException());

            // Act
            ResponseEntity response = applicationController.approveApplication(validJWT, applicationId, status);

            // Assert
            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found.", response.getBody());
        }
    }

    @Test
    public void testApproveApplication_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        Long applicationId = 1L;
        String status = "Approved";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act
            ResponseEntity response = applicationController.approveApplication(invalidJWT, applicationId, status);

            // Assert
            assertEquals(401, response.getStatusCodeValue());
        }
    }
}
