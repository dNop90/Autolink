package com.example.project2.Services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Response.AccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Unit test for "register" method
     */
    @Test
    public void testRegister_Success() throws Exception {
        // Arrange
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setUsername("testuser");
        account.setPassword("password");
        when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(null);
        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(null);

        // Act
        String result = accountService.register(account);

        // Assert
        assertEquals("Success", result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testRegister_DuplicateEmail_ThrowsException() {
        // Arrange
        Account account = new Account();
        account.setEmail("test@example.com");
        when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(new Account());

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> accountService.register(account));
    }

    @Test
    public void testRegister_DuplicateUsername_ThrowsException() {
        // Arrange
        Account account = new Account();
        account.setUsername("testuser");
        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(new Account());

        // Act & Assert
        assertThrows(DuplicateUsernameException.class, () -> accountService.register(account));
    }

    /**
     * Unit test for "login" method
     */
    @Test
    public void testLogin_Success() throws Exception {
        // Arrange
        Account account = new Account();
        account.setUsername("testuser");
        account.setPassword("password");
        Account storedAccount = new Account();
        storedAccount.setUsername("testuser");
        storedAccount.setPassword(AccountService.toHexString(AccountService.getSHA("password")));
        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(storedAccount);

        // Act
        AccountResponse response = accountService.login(account);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    public void testLogin_AccountNotFound_ThrowsException() {
        // Arrange
        Account account = new Account();
        account.setUsername("testuser");
        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(null);

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.login(account));
    }

    @Test
    public void testLogin_IncorrectPassword_ThrowsException() throws Exception {
        // Arrange
        Account account = new Account();
        account.setUsername("testuser");
        account.setPassword("wrongpassword");
        Account storedAccount = new Account();
        storedAccount.setUsername("testuser");
        storedAccount.setPassword(AccountService.toHexString(AccountService.getSHA("password")));
        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(storedAccount);

        // Act & Assert
        assertThrows(PasswordIncorrectException.class, () -> accountService.login(account));
    }

    /**
     * Unit test for "getCurrentUser" method
     */
    @Test
    public void testGetCurrentUser_Success() {
        // Arrange
        String username = "testuser";
        String token = "valid.token";
        Account storedAccount = new Account();
        storedAccount.setUsername(username);
        when(accountRepository.findAccountByUsername(username)).thenReturn(storedAccount);

        // Act
        AccountResponse response = accountService.getCurrentUser(username, token);

        // Assert
        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertEquals(token, response.getToken());
    }

    /**
     * Unit test for "getUserByUsername" method
     */
    @Test
    public void testGetUserByUsername_Success() throws AccountNotFoundException {
        // Arrange
        String username = "testuser";
        Account storedAccount = new Account();
        storedAccount.setUsername(username);
        when(accountRepository.findAccountByUsername(username)).thenReturn(storedAccount);

        // Act
        AccountResponse response = accountService.getUserByUsername(username);

        // Assert
        assertNotNull(response);
        assertEquals(username, response.getUsername());
    }

    @Test
    public void testGetUserByUsername_AccountNotFound_ThrowsException() {
        // Arrange
        String username = "testuser";
        when(accountRepository.findAccountByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.getUserByUsername(username));
    }

    /**
     * Unit test for "promote" method
     */
    @Test
    public void testPromote_Success() throws AccountNotFoundException {
        // Arrange
        String username = "testuser";
        Account storedAccount = new Account();
        storedAccount.setUsername(username);
        when(accountRepository.findAccountByUsername(username)).thenReturn(storedAccount);

        // Act
        String result = accountService.promote(username);

        // Assert
        assertEquals("Promoted", result);
        verify(accountRepository, times(1)).updateRole(storedAccount.getAccountId(), 3);
    }

    @Test
    public void testPromote_AccountNotFound_ThrowsException() {
        // Arrange
        String username = "testuser";
        when(accountRepository.findAccountByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.promote(username));
    }

    @Test
    public void testUpdatePassword_Success() throws AccountNotFoundException, PasswordIncorrectException, NoSuchAlgorithmException {
        Account account = new Account();
        account.setUsername("testuser");
        account.setCurrentPassword("oldpassword");
        account.setNewPassword("newpassword");

        Account storedAccount = new Account();
        storedAccount.setUsername("testuser");
        storedAccount.setPassword(AccountService.toHexString(AccountService.getSHA("oldpassword")));

        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(storedAccount);

        accountService.updatePassword(account);

        verify(accountRepository).updatePassword(eq(storedAccount.getAccountId()), anyString());
    }

    @Test
    public void testUpdatePassword_AccountNotFound() {
        Account account = new Account();
        account.setUsername("nonexistentUser");

        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.updatePassword(account));
    }

    @Test
    public void testUpdatePassword_InvalidCurrentPassword() throws NoSuchAlgorithmException {
        Account account = new Account();
        account.setUsername("testuser");
        account.setCurrentPassword("wrongpassword");

        Account storedAccount = new Account();
        storedAccount.setUsername("testuser");
        storedAccount.setPassword(AccountService.toHexString(AccountService.getSHA("correctpassword")));

        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(storedAccount);

        assertThrows(PasswordIncorrectException.class, () -> accountService.updatePassword(account));
    }

    @Test
    public void testSuspend_Success() throws AccountNotFoundException {
        Account account = new Account();
        account.setUsername("testuser");

        when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(account);

        String result = accountService.suspend(account.getUsername(), true);

        assertEquals("Suspended", result);
        verify(accountRepository).suspend(account.getAccountId(), true);
    }

    @Test
    public void testSuspend_AccountNotFound() {
        when(accountRepository.findAccountByUsername("nonexistentUser")).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.suspend("nonexistentUser", true));
    }
}
