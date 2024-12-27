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
    private Integer role;
    private Boolean isSuspended;
    private Long imageId;
    private String token;

    
    @JsonCreator
    /*
     * Account fields returned to front end after login, hiding password and other information
     */
    public AccountResponse(
        @JsonProperty("accountId")  Long accountId,
        @JsonProperty("username") String username,
        @JsonProperty("role") Integer role,
        @JsonProperty("isSuspended") Boolean isSuspended,
        @JsonProperty("imageId") Long imageId,
        @JsonProperty("token") String token
    ) {
        this.accountId = accountId;
        this.username = username;
        this.role = role;
        this.isSuspended = isSuspended;
        this.imageId = imageId;
        this.token = token;
    }
    
    
}
