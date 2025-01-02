package com.example.project2.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.project2.Entities.Message;

@Service
public class WebSocketService {

    @Autowired
    private SocketIOServer socketIOServer;
    
    public void sendMessage(Message message){
        Map<String, Object> data = new HashMap<>();
        data.put("messageId", message.getMessageId());
        data.put("fromAccountID", message.getFromAccountID());
        data.put("fromUsername", message.getFromUsername());
        data.put("toAccountID", message.getToAccountID());
        data.put("toUsername", message.getFromUsername());
        data.put("message", message.getMessage());
        data.put("createAt", message.getCreateAt().toString());

        socketIOServer.getBroadcastOperations().sendEvent("message", data);
    }
}
