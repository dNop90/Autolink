package com.example.project2.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
/*
 * Account entity
 */
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private String username;
    private String email;
    private String password;
    private Integer role = 1; // 1 = regular user, 2 = dealer, 3 = admin, default regular user
    private Boolean isSuspended = false;
    private String firstName;
    private String lastName;
    private String phone;
    private Long imageId;
}
