package com.example.project2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.project2.Entities.Vehicle;
import com.example.project2.Services.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    /*
     * List all vehicle inventory
     * 
     * @param
     * 
     * @return list of all vehicles
     */

    @CrossOrigin(origins = "http://localhost:3000")
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
    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
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
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    /*
     * Delete a vehicle from the database
     * 
     * @param id the id of the vehicle to be removed.
     * 
     * @return remove the vehicle and save the data.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id);
    }
}
