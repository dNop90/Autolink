package com.example.project2.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public ProfileResponse(
        @JsonProperty("username") String username,
        @JsonProperty("email")  String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("phone") String phone
    ) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
