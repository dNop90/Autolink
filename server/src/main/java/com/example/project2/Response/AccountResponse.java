package com.example.project2.Response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountResponse {
    private Long accountId;
    private String username;
    private String email;
    private Integer role;
    private Boolean isSuspended;
    private String firstName;
    private String lastName;
    private String phone;
    private Long imageId;

    
    @JsonCreator
    public AccountResponse(
        @JsonProperty("accountId")  Long accountId,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("role") Integer role,
        @JsonProperty("isSuspended") Boolean isSuspended,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("phone") String phone,
        @JsonProperty("imageId") Long imageId
    ) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isSuspended = isSuspended;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.imageId = imageId;
    }
    
    
}
