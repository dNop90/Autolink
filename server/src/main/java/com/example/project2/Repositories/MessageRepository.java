package com.example.project2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project2.Entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    
}
