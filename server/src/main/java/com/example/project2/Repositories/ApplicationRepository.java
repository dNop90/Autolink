package com.example.project2.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project2.Entities.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    
    @Query("SELECT a FROM Application a WHERE status = ?1 ORDER BY applicationId DESC")
    List<Application> getPendingApplication(String status);

    @Query("SELECT a FROM Application a WHERE applicantId = ?1 ORDER BY applicationId DESC")
    List<Application> getApplicationByAccountId(Integer applicantId);

    @Query("SELECT applicantId FROM Application a WHERE applicationId = ?1")
    Long findAccountId(Long applicationId);

    @Modifying
    @Query("UPDATE Application SET status = ?2 WHERE applicationId = ?1")
    int updateStatus(Long applicationId, String status);
}
