package com.example.project2.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import jakarta.annotation.PreDestroy;

@CrossOrigin
@Component
public class WebsocketConfig{
    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

        server = new SocketIOServer(config);
        server.start();

        return server;
    }

    @PreDestroy
	public void stopSocketIOServer() {
		this.server.stop();
	}
}
