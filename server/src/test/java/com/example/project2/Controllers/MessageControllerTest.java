package com.example.project2.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.project2.Entities.Message;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Services.MessageService;

import io.jsonwebtoken.Claims;

public class MessageControllerTest {
    @Mock
    MessageService messageService;

    @InjectMocks
    MessageController messageController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessage_Success() throws Exception {
        String validJWT = "Bearer valid.jwt.token";

        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT))
                .thenReturn(true);
            
            when(messageService.createMessage(any(Message.class))).thenReturn(true);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("fromID", 6);
            payload.put("toID", 5);
            payload.put("message", "Testing");

            ResponseEntity<Object> response = messageController.sendMessage(validJWT, payload);
            
            assertEquals(200, response.getStatusCode().value());
        }
    }

    @Test
    public void testSendMessage_Fail() throws Exception {
        String validJWT = "Bearer valid.jwt.token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT))
                .thenReturn(true);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("fromID", 6);
            payload.put("message", "Testing");

            ResponseEntity<Object> response = messageController.sendMessage(validJWT, payload);
            
            assertNotEquals(200, response.getStatusCode().value());
        }
    }

    @Test
    public void testGetOldMessages_Failed() throws Exception {
        String validJWT = "Bearer valid.jwt.token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT))
                .thenReturn(true);

            List<Message> message = new ArrayList<Message>();
            when(messageService.getOldMessages(anyLong(), anyLong(), anyInt())).thenReturn(message);
            
            Map<String, Object> payload = new HashMap<>();

            ResponseEntity<Object> response = messageController.getOldMessages(validJWT, payload);
            
            assertNotEquals(200, response.getStatusCode().value());
        }
    }

    @Test
    public void testGetUsers_Failed() throws Exception {
        String validJWT = "Bearer valid.jwt.token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT))
                .thenReturn(false);

            ResponseEntity<Object> response = messageController.getUsers(validJWT);
            
            assertNotEquals(200, response.getStatusCode().value());
        }
    }
}
