package com.example.project2.Controllers;

import com.example.project2.Services.CarApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class CarApiController {

    @Autowired
    private CarApiService carApiService;

    // Endpoint to authenticate and fetch JWT
    @PostMapping("/auth/login")
    public ResponseEntity<String> authenticate() {
        String jwt = carApiService.authenticate();
        return ResponseEntity.ok(jwt);
    }

    // Proxy API requests to CarAPI
    @GetMapping("/{endpoint}")
    public ResponseEntity<String> proxyRequest(
            @PathVariable("endpoint") String endpoint,
            @RequestParam Map<String, String> queryParams) {
        return carApiService.proxyRequest(endpoint, queryParams);
    }
}
