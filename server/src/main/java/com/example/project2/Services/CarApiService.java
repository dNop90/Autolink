package com.example.project2.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CarApiService {

    @Value("${carapi.api-token}")
    private String apiToken;

    @Value("${carapi.api-secret}")
    private String apiSecret;

    @Value("${carapi.base-url}")
    private String carApiBaseUrl;

    private String jwtToken; // Cached JWT

    private final RestTemplate restTemplate;

    public CarApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String authenticate() {
        String url = carApiBaseUrl + "/auth/login";

        // Request body
        Map<String, String> requestBody = Map.of(
                "api_token", apiToken,
                "api_secret", apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                this.jwtToken = response.getBody(); // Cache the JWT
                return jwtToken;
            } else {
                throw new RuntimeException("Failed to authenticate: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error authenticating with CarAPI: " + e.getMessage());
        }
    }

    public ResponseEntity<String> proxyRequest(String endpoint, Map<String, String> queryParams) {
        if (jwtToken == null) {
            authenticate(); // Authenticate if JWT is missing
        }

        String queryString = queryParams.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");

        String url = carApiBaseUrl + "/" + endpoint + "?" + queryString;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from CarAPI: " + e.getMessage());
        }
    }
}
