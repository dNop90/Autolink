package com.example.project2.Services;

import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Response.AccountResponse;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    /*
     * Register service
     * 
     * @param account, account with fields of username, email, and password
     * 
     * @return success message
     * 
     * @throws DuplicateEmailException if email already exist in db,
     * DuplicateUsernameException if username already in db,
     * NoSuchAlgorithmException if hashing algorithm doesn't exist
     */

    public String register(Account account)
            throws DuplicateEmailException, DuplicateUsernameException, NoSuchAlgorithmException {

        Account check = accountRepository.findAccountByEmail(account.getEmail());
        if (check != null)
            throw new DuplicateEmailException();
        check = accountRepository.findAccountByUsername(account.getUsername());
        if (check != null)
            throw new DuplicateUsernameException();

        account.setPassword(toHexString(getSHA(account.getPassword())));
        accountRepository.save(account);
        return "Success";

    }

    /*
     * Login service
     * 
     * @param account, account with fields of username, email, and password
     * 
     * @return result, an account response with necesary fields
     * 
     * @throws AccountNotFoundException if account not in db,
     * PasswordIncorrectException if password does not match with password in db,
     * NoSuchAlgorithmException if hashing algorithm doesn't exist
     */
    public AccountResponse login(Account account)
            throws AccountNotFoundException, PasswordIncorrectException, NoSuchAlgorithmException {
        Account check = accountRepository.findAccountByUsername(account.getUsername());

        if (check == null)
            throw new AccountNotFoundException();

        account.setPassword(toHexString(getSHA(account.getPassword())));

        if (!check.getPassword().equals(account.getPassword()))
            throw new PasswordIncorrectException();
        String token = JWTUtil.generateToken(check.getUsername(), String.valueOf(check.getAccountId()));
        AccountResponse result = new AccountResponse(
                check.getAccountId(),
                check.getUsername(),
                check.getRole(),
                check.getIsSuspended(),
                check.getImageId(),
                token);
        return result;
    }

    public AccountResponse getCurrentUser(String username, String token) {
        Account check = accountRepository.findAccountByUsername(username);
        AccountResponse result = new AccountResponse(
                check.getAccountId(),
                check.getUsername(),
                check.getRole(),
                check.getIsSuspended(),
                check.getImageId(),
                token);

        return result;
    }

    public AccountResponse getUserByUsername(String username) throws AccountNotFoundException {
        System.out.println(username);
        Account check = accountRepository.findAccountByUsername(username);
        if (check == null)
            throw new AccountNotFoundException();
        AccountResponse result = new AccountResponse(
                check.getAccountId(),
                check.getUsername(),
                check.getRole(),
                check.getIsSuspended(),
                null,
                null);
        return result;
    }

    public Optional<Account> getUserByUsernameOrEmail(String usernameOrEmail) {
        return accountRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    // Update user profile
    public void updateUserProfile(Long userId, Account updateUserDTO) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(userId.longValue());
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }

        Account account = optionalAccount.get();
        account.setFirstName(updateUserDTO.getFirstName());
        account.setLastName(updateUserDTO.getLastName());
        account.setEmail(updateUserDTO.getEmail());
        account.setPhone(updateUserDTO.getPhone());

        accountRepository.save(account); // Save the updated account
    }

    public void updatePassword(Long accountId, String newPassword) throws AccountNotFoundException, NoSuchAlgorithmException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        
        // If account is not found, throw an exception
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }
        
        // Get the account from the database
        Account account = optionalAccount.get();
        
        // Hash the new password before saving it
        account.setPassword(toHexString(getSHA(newPassword))); // Assuming your password is hashed
        accountRepository.save(account); // Save the updated account
    }

    public boolean validatePassword(String username, String currentPassword) throws NoSuchAlgorithmException {
        Account account = accountRepository.findAccountByUsername(username);
        
        if (account == null) {
            return false;  // Account not found
        }
        
        // Check if the current password matches the stored password (hashed)
        return account.getPassword().equals(toHexString(getSHA(currentPassword)));
    }

    /*
     * Promotion service to change regular account to admin account
     * 
     * @param username, username of the account to be promoted
     * @return Promoted message
     * @throws AccountNotFoundException if username not in database
     */
    public String promote(String username) throws AccountNotFoundException{
        Account a = accountRepository.findAccountByUsername(username);
        if (a != null) {
            accountRepository.updateRole(a.getAccountId(), 3);
            return "Promoted";
        } else {
            throw new AccountNotFoundException();
        }
    }

    /*
     * Helper class for password hasing
     */
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /*
     * Helper class for password hasing
     */
    private static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public List<Account> getAll() {
        // TODO Auto-generated method stub
        return accountRepository.findAll();
    }
}
