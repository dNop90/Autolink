package com.example.project2.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Vehicle;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVehicles() {
        // Arrange
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2);
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.getAllVehicles();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetVehicleById_Success() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Act
        ResponseEntity<Vehicle> response = vehicleService.getVehicleById(vehicleId);

        // Assert
        assertEquals(ResponseEntity.ok(vehicle), response);
    }

    @Test
    public void testGetVehicleById_NotFound() {
        // Arrange
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Vehicle> response = vehicleService.getVehicleById(vehicleId);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testCreateVehicle_Success() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        Long dealerId = 1L;
        Account dealer = new Account();
        dealer.setAccountId(dealerId);
        when(accountRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        // Act
        Vehicle result = vehicleService.createVehicle(vehicle, dealerId);

        // Assert
        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    public void testCreateVehicle_DealerNotFound() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        Long dealerId = 1L;
        when(accountRepository.findById(dealerId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.createVehicle(vehicle, dealerId);
        });
        assertEquals("Dealer not found with ID: " + dealerId, exception.getMessage());
    }

    @Test
    public void testUpdateVehicle_Success() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setVehicleId(vehicleId);
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setModel("New Model");
        updatedVehicle.setPrice(20000L); // Change this to Long
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        // Act
        ResponseEntity<Vehicle> response = vehicleService.updateVehicle(vehicleId, updatedVehicle);

        // Assert
        assertEquals(ResponseEntity.ok(existingVehicle), response);
        assertEquals("New Model", existingVehicle.getModel());
        assertEquals(Long.valueOf(20000), existingVehicle.getPrice()); // Ensure this matches the type
    }

    @Test
    public void testUpdateVehicle_NotFound() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle updatedVehicle = new Vehicle();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Vehicle> response = vehicleService.updateVehicle(vehicleId, updatedVehicle);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testDeleteVehicle_Success() {
        // Arrange
        Long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = vehicleService.deleteVehicle(vehicleId);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(vehicleRepository, times(1)).deleteById(vehicleId);
    }

    @Test
    public void testDeleteVehicle_NotFound() {
        // Arrange
        Long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = vehicleService.deleteVehicle(vehicleId);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
        verify(vehicleRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testGetBuyerIdsByAccountId() {
        // Arrange
        Long accountId = 1L;
        List<Long> buyerIds = Arrays.asList(1L, 2L);
        when(vehicleRepository.findBuyerIdsByAccountId(accountId)).thenReturn(buyerIds);

        // Act
        List<Long> result = vehicleService.getBuyerIdsByAccountId(accountId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }
}