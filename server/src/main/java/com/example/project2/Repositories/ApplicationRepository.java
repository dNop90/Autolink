package com.example.project2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project2.Entities.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{

}
