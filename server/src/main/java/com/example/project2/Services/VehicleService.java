package com.example.project2.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.project2.Entities.Vehicle;
import com.example.project2.Repositories.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    /*
     * @return all vehicles in the inventory
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /*
     * get the vehicle by passing in the id
     * 
     * @param id the id of the vehicle
     * 
     * @return vehicle that matches that id
     */
    public ResponseEntity<Vehicle> getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /*
     * Create a vehicle or add new vehicle in the inventory
     * 
     * @param Vehicle an object of the vehicle
     * 
     * @return save the vehicle to the database.
     */
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
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
    public ResponseEntity<Vehicle> updateVehicle(Long id, Vehicle vehicleDetails) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setModel(vehicleDetails.getModel());
            vehicle.setPrice(vehicleDetails.getPrice());
            vehicle.setCondition(vehicleDetails.getCondition());
            vehicle.setYear(vehicleDetails.getYear());
            vehicle.setMake(vehicleDetails.getMake());
            vehicle.setColor(vehicleDetails.getColor());
            vehicle.setEngineType(vehicleDetails.getEngineType());
            vehicle.setImgUrl(vehicleDetails.getImgUrl());
            return ResponseEntity.ok(vehicleRepository.save(vehicle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Delete a vehicle from the database
     * 
     * @param id the id of the vehicle to be removed.
     * 
     * @return remove the vehicle and save the data.
     */
    public ResponseEntity<Void> deleteVehicle(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}