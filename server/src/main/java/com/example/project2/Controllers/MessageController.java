package com.example.project2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.Services.KafkaService;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private KafkaService kafkaService;

    
}
