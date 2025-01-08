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

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ReviewService reviewService;

    /*
     * List all vehicle inventory
     * 
     * @param
     * 
     * @return list of all vehicles
     */

    @GetMapping("/inventory")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    /*
     * get a detail of a vehicle
     * 
     * @param id an id of a vehicle
     * 
     * @return a vehicle that matches the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    /*
     * Create a vehicle
     * 
     * @param Vehicle Object an object that is passed (A vehicle object)
     * 
     * @return add a new vehicle
     */
    // @PostMapping
    // public Vehicle createVehicle(@RequestHeader("Authorization") String
    // authHeader, @RequestBody Vehicle vehicle) {
    // System.out.println("Received Vehicle: " + vehicle);
    // if (JWTUtil.isValid(authHeader)) {
    // try {
    // return vehicleService.createVehicle(vehicle);
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error
    // saving vehicle", e);
    // }
    // }

    // return ResponseEntity.status(401).build();

    // }
    // @PostMapping
    // public Vehicle createVehicle(@RequestHeader("Authorization") String
    // authHeader, @RequestBody Vehicle vehicle,
    // @RequestParam Long dealerId) {
    // System.out.println("Received Vehicle: " + vehicle);

    // JWTUtil.parseToken(authHeader).getId();
    // // Check if the authorization header is valid
    // if (JWTUtil.isValid(authHeader)) {
    // try {
    // return vehicleService.createVehicle(vehicle, dealerId);
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error
    // saving vehicle", e);
    // }
    // } else {
    // // If the authHeader is not valid, throw a 401 Unauthorized exception
    // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid
    // authorization header");
    // }
    // }
    @PostMapping
    public Vehicle createVehicle(@RequestHeader("Authorization") String authHeader, @RequestBody Vehicle vehicle,
            @RequestParam Long dealerId) {
        System.out.println("Received Vehicle: " + vehicle);

        // Check if the authorization header is valid
        if (!JWTUtil.isValid(authHeader)) {
            // If the authHeader is not valid, throw a 401 Unauthorized exception
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

        // Now that we know the JWT is valid, we can safely parse it
        Claims claims = JWTUtil.parseToken(authHeader);
        if (claims == null) {
            // Handle the case where parsing the token returns null
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

        // Proceed to create the vehicle
        try {
            return vehicleService.createVehicle(vehicle, dealerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving vehicle", e);
        }
    }

    @GetMapping("/buyers/{accountId}")
    public List<Long> getBuyerIdsByAccountId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long accountId) {

        // Validate the authorization header
        if (JWTUtil.isValid(authHeader)) {
            return vehicleService.getBuyerIdsByAccountId(accountId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }
    }

    /*
     * Update a vehicle
     * 
     * @param id the id of the object(vehicle) that is going to be updated
     * 
     * @param vehicleDetails an object of the Vehicle.
     * 
     * @return change the old data and new data (updated version is saved)
     */
    // @PutMapping("/{id}")
    // public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id,
    // @RequestBody Vehicle vehicle) {
    // return vehicleService.updateVehicle(id, vehicle);
    // }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseEntity<Vehicle>> updateVehicle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {

        // Check if the authorization header is valid
        if (JWTUtil.isValid(authHeader)) {
            try {
                ResponseEntity<Vehicle> updatedVehicle = vehicleService.updateVehicle(id, vehicle);
                return ResponseEntity.ok(updatedVehicle);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating vehicle", e);
            }
        } else {
            // If the authHeader is not valid, throw a 401 Unauthorized exception
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }
    }

    /*
     * Delete a vehicle from the database
     * 
     * @param id the id of the vehicle to be removed.
     * 
     * @return remove the vehicle and save the data.
     */
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
    // return vehicleService.deleteVehicle(id);
    // }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        // Check if the authorization header is valid
        if (JWTUtil.isValid(authHeader)) {
            try {
                vehicleService.deleteVehicle(id);
                return ResponseEntity.noContent().build(); // Return 204 No Content
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting vehicle", e);
            }
        } else {
            // If the authHeader is not valid, throw a 401 Unauthorized exception
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

    }

}