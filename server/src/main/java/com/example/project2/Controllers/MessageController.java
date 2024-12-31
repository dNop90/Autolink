package com.example.project2.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.Entities.Message;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.MessageService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping()
    public ResponseEntity<Object> sendMessage(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> payload) {
        if (!JWTUtil.isValid(authHeader)) {
            return ResponseEntity.status(401).build();
        }

        if(payload == null)
        {
            return ResponseEntity.status(400).build();
        }

        //Check if it contain the field
        if(payload.get("fromID") == null || payload.get("toID") == null || payload.get("message") == null)
        {
            return ResponseEntity.status(400).build();
        }

        Long fromAccountID = Long.valueOf(payload.get("fromID").toString());
        Long toAccountID = Long.valueOf(payload.get("toID").toString());
        String strMessage = payload.get("message").toString();

        Message message = new Message(fromAccountID, toAccountID, strMessage);

        //If message successfully create then we will send 200 status
        if(messageService.createMessage(message))
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(400).build();
    }
    
    
}
