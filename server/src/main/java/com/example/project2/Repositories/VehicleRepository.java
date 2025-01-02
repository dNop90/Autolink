package com.example.project2.Repositories;

import com.example.project2.Entities.Vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("SELECT v.buyer.accountId FROM Vehicle v WHERE v.buyer.accountId = :accountId")
    List<Long> findBuyerIdsByAccountId(@Param("accountId") Long accountId);
}
