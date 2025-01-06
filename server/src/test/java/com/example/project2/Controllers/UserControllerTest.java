package com.example.project2.Controllers;

import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Response.AccountResponse;
import com.example.project2.Response.ProfileResponse;
import com.example.project2.Services.AccountService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for "register" endpoint
     */
    @Test
    public void testRegister_Success() throws Exception {
        // Arrange
        Account account = new Account();
        when(accountService.register(account)).thenReturn("Success");

        // Act
        ResponseEntity response = userController.register(account);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody());
    }

    @Test
    public void testRegister_DuplicateUsername() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new DuplicateUsernameException()).when(accountService).register(account);

        // Act
        ResponseEntity response = userController.register(account);

        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Username taken", response.getBody());
    }

    @Test
    public void testRegister_DuplicateEmail() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new DuplicateEmailException()).when(accountService).register(account);

        // Act
        ResponseEntity response = userController.register(account);

        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Email already registered", response.getBody());
    }

    @Test
    public void testRegister_NoSuchAlgorithm() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new NoSuchAlgorithmException()).when(accountService).register(account);

        // Act
        ResponseEntity response = userController.register(account);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Hashing algorithm not found", response.getBody());
    }

    /**
     * Test for "login" endpoint
     */
    @Test
    public void testLogin_Success() throws Exception {
        // Arrange
        Account account = new Account();
        AccountResponse accountResponse = new AccountResponse(1L, "user", 1, false, "token");
        when(accountService.login(account)).thenReturn(accountResponse);

        // Act
        ResponseEntity response = userController.login(account);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    public void testLogin_AccountNotFound() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new AccountNotFoundException()).when(accountService).login(account);

        // Act
        ResponseEntity response = userController.login(account);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("No account with username", response.getBody());
    }

    @Test
    public void testLogin_InvalidPassword() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new PasswordIncorrectException()).when(accountService).login(account);

        // Act
        ResponseEntity response = userController.login(account);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid password", response.getBody());
    }

    @Test
    public void testLogin_NoSuchAlgorithm() throws Exception {
        // Arrange
        Account account = new Account();
        doThrow(new NoSuchAlgorithmException()).when(accountService).login(account);

        // Act
        ResponseEntity response = userController.login(account);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Hashing algorithm not found", response.getBody());
    }

    /**
     * Test for "getUserProfile" endpoint
     */
    @Test
    public void testGetUserProfile_ValidJWT() throws Exception {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        String username = "testuser";
        ProfileResponse profileResponse = new ProfileResponse("testuser", "email@test.com", "First", "Last", "123456");
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(accountService.getUserProfileByUsername(username)).thenReturn(profileResponse);

            // Act
            ResponseEntity response = userController.getUserProfile(validJWT, username);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(profileResponse, response.getBody());
        }
    }

    @Test
    public void testGetUserProfile_InvalidJWT() {
        // Arrange
        String invalidJWT = "Bearer invalid.jwt.token";
        String username = "testuser";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(invalidJWT)).thenReturn(false);

            // Act
            ResponseEntity response = userController.getUserProfile(invalidJWT, username);

            // Assert
            assertEquals(401, response.getStatusCodeValue());
        }
    }

    @Test
    public void testGetUserProfile_UserNotFound() throws Exception {
        // Arrange
        String validJWT = "Bearer valid.jwt.token";
        String username = "testuser";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).getUserProfileByUsername(username);

            // Act
            ResponseEntity response = userController.getUserProfile(validJWT, username);

            // Assert
            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
        }
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        String validJWT = "Bearer valid.jwt.token";
        Account account = new Account();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);

            ResponseEntity response = userController.changePassword(validJWT, account);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Password updated", response.getBody());
        }
    }

    @Test
    public void testChangePassword_InvalidJWT() throws Exception {
        String inValidJWT = "Bearer inValid.jwt.token";
        Account account = new Account();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(inValidJWT)).thenReturn(false);

            ResponseEntity response = userController.changePassword(inValidJWT, account);

            assertEquals(401, response.getStatusCodeValue());
        }
    }

    @Test
    public void testChangePassword_InvalidPassword() throws Exception {
        String validJWT = "valid-token";
        Account account = new Account();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new PasswordIncorrectException()).when(accountService).updatePassword(account);

            ResponseEntity response = userController.changePassword(validJWT, account);

            assertEquals(401, response.getStatusCodeValue());
            assertEquals("Invalid password", response.getBody());
        }
    }

    @Test
    public void testChangePassword_NoSuchAlgorithm() throws Exception {
        String validJWT = "valid-token";
        Account account = new Account();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new NoSuchAlgorithmException()).when(accountService).updatePassword(account);

            ResponseEntity response = userController.changePassword(validJWT, account);

            assertEquals(500, response.getStatusCodeValue());
            assertEquals("Hashing algorithm not found", response.getBody());
        }
    }

    @Test
    public void testChangePassword_UserNotFound() throws Exception {
        String validJWT = "valid-token";
        Account account = new Account();
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).updatePassword(account);

            ResponseEntity response = userController.changePassword(validJWT, account);

            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
        }
    }

    @Test
    public void testPromote_Success() throws AccountNotFoundException {
        String username = "testuser";
        String validJWT = "valid-token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(accountService.promote(username)).thenReturn("Promoted");

            ResponseEntity response = userController.promote(validJWT, username);

            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertEquals("Promoted", response.getBody());
        }
    }

    @Test
    public void testPromote_UserNotFound() throws Exception {
        String validJWT = "valid-token";
        String username = "nonexistentUser";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).promote(username);

            ResponseEntity response = userController.promote(validJWT, username);

            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
        }
    }

    @Test
    public void testUpdateProfile_Success() throws Exception {
        String validJWT = "valid-token";
        ProfileResponse profile = new ProfileResponse(null, null, null, null, null);
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);

            ResponseEntity response = userController.updateUserProfile(validJWT, profile);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Account updated", response.getBody());
        }
    }

    @Test
    public void testUpdateProfile_UserNotFound() throws Exception {
        String validJWT = "valid-token";
        ProfileResponse profile = new ProfileResponse(null, null, null, null, null);
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).updateUserProfile(profile);

            ResponseEntity response = userController.updateUserProfile(validJWT, profile);

            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
        }
    }

    @Test
    public void testUpdateProfile_InvalidJWT() throws Exception {
        String validJWT = "valid-token";
        ProfileResponse profile = new ProfileResponse(null, null, null, null, null);
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(false);

            ResponseEntity response = userController.updateUserProfile(validJWT, profile);

            assertEquals(401, response.getStatusCodeValue());
        }
    }

    @Test
    public void testSearch_Success() throws Exception {
        String validJWT = "valid-token";
        String username = "username";
        AccountResponse accountResponse = new AccountResponse(1L, "user", 1, false, null);
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(accountService.getUserByUsername(username)).thenReturn(accountResponse);
            ResponseEntity response = userController.getUser(validJWT, username);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(accountResponse, response.getBody());
        }
    }

    @Test
    public void testSearch_InvalidJWT() throws Exception {
        String validJWT = "valid-token";
        String username = "username";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(false);
            ResponseEntity response = userController.getUser(validJWT, username);

            assertEquals(401, response.getStatusCodeValue());
        }
    }

    @Test
    public void testSearch_AccountNotFound() throws Exception {
        String validJWT = "valid-token";
        String username = "username";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).getUserByUsername(username);
            ResponseEntity response = userController.getUser(validJWT, username);

            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
        }
    }

    @Test
    public void testSuspend_Suspend() throws Exception {
        String validJWT = "valid-token";
        Boolean status = true;
        String username = "username";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            when(accountService.suspend(username, status)).thenReturn("Suspended");
            ResponseEntity response = userController.suspend(validJWT, status, username);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Suspended", response.getBody());
            ;
        }
    }

    @Test
    public void testSuspend_AccountNotFound() throws Exception {
        String validJWT = "valid-token";
        Boolean status = true;
        String username = "username";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(true);
            doThrow(new AccountNotFoundException()).when(accountService).suspend(username, status);
            ResponseEntity response = userController.suspend(validJWT, status, username);
            assertEquals(404, response.getStatusCodeValue());
            assertEquals("User not found", response.getBody());
            ;
        }
    }

    @Test
    public void testSuspend_InvalidJWT() throws Exception {
        String validJWT = "valid-token";
        Boolean status = true;
        String username = "username";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(false);
            ResponseEntity response = userController.suspend(validJWT, status, username);
            assertEquals(401, response.getStatusCodeValue());
        }
    }

    @Test
    public void testToken_InvalidJWT() throws Exception {
        String validJWT = "valid-token";
        try (MockedStatic<JWTUtil> utilities = Mockito.mockStatic(JWTUtil.class)) {
            utilities.when(() -> JWTUtil.isValid(validJWT)).thenReturn(false);
            ResponseEntity response = userController.verifyUserToken(validJWT);
            assertEquals(401, response.getStatusCodeValue());
        }
    }
}
