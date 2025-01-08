package com.example.project2.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.example.project2.Services.CarApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class CarApiControllerTest {

    @Mock
    private CarApiService carApiService;

    @InjectMocks
    private CarApiController carApiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticate_Success() {
        // Arrange
        String expectedJwt = "testJwtToken";
        when(carApiService.authenticate()).thenReturn(expectedJwt);

        // Act
        ResponseEntity<String> response = carApiController.authenticate();

        // Assert
        assertEquals(ResponseEntity.ok(expectedJwt), response);
    }

    @Test
    public void testProxyRequest_Success() {
        // Arrange
        String endpoint = "vehicles";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param1", "value1");
        String expectedResponse = "response data";
        when(carApiService.proxyRequest(endpoint, queryParams)).thenReturn(ResponseEntity.ok(expectedResponse));

        // Act
        ResponseEntity<String> response = carApiController.proxyRequest(endpoint, queryParams);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void testProxyRequest_Error() {
        // Arrange
        String endpoint = "vehicles";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param1", "value1");
        when(carApiService.proxyRequest(endpoint, queryParams)).thenThrow(new RuntimeException("Error fetching data"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carApiController.proxyRequest(endpoint, queryParams);
        });
        assertEquals("Error fetching data", exception.getMessage());
    }
}