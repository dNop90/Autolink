package com.example.project2.Controllers;

import java.util.List;
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
        boolean service_result = messageService.createMessage(message);
        if(service_result)
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(400).build();
    }
    
    /**
     * Get old messages
     * @param authHeader User token
     * @param payload The payload containing from which user ID to look for
     * @return A list of old messages if success. Otherwise error status
     */
    @PostMapping("/old")
    public ResponseEntity<Object> getOldMessages(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> payload) {
        if (!JWTUtil.isValid(authHeader)) {
            return ResponseEntity.status(401).build();
        }

        if(payload == null)
        {
            return ResponseEntity.status(400).build();
        }

        //Check if it contain the field
        if(payload.get("fromID") == null)
        {
            return ResponseEntity.status(400).build();
        }

        Long fromAccountID = Long.valueOf(payload.get("fromID").toString());
        Long toAccountID = Long.valueOf(JWTUtil.parseToken(authHeader).getId());
        int offset = 0;

        if(payload.get("offset") != null)
        {
            offset = Integer.valueOf(payload.get("offset").toString());
        }

        List<Message> OldMessages = messageService.getOldMessages(fromAccountID, toAccountID, offset);
        return ResponseEntity.status(200).body(OldMessages);
    }
    
    /**
     * Get a list of unique users that that the user had chat with
     * @param authHeader User token to get user ID
     * @return A list of messages containing unique users. Otherwise error status
     */
    @PostMapping("/users")
    public ResponseEntity<Object> getUsers(@RequestHeader("Authorization") String authHeader) {
        if (!JWTUtil.isValid(authHeader)) {
            return ResponseEntity.status(401).build();
        }

        Long fromAccountID = Long.valueOf(JWTUtil.parseToken(authHeader).getId());

        List<?> OldMessages = messageService.getUniqueAccountInteractWith(fromAccountID);
        return ResponseEntity.status(200).body(OldMessages);
    }
}
