package com.example.project2.Repositories;

import com.example.project2.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Change the method name to findByVehicle_VehicleId
    List<Review> findByVehicle_VehicleId(Long vehicleId);
}