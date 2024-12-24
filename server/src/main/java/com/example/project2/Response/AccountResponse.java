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
    /*
     * Account fields returned to front end after login, hiding password and other information
     */
    public AccountResponse(
        @JsonProperty("accountId")  Long accountId,
        @JsonProperty("username") String username,
        @JsonProperty("role") Integer role,
        @JsonProperty("isSuspended") Boolean isSuspended,
        @JsonProperty("imageId") Long imageId
    ) {
        this.accountId = accountId;
        this.username = username;
        this.role = role;
        this.isSuspended = isSuspended;
        this.imageId = imageId;
    }
    
    
}
