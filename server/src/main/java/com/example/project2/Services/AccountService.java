package com.example.project2.Services;

import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Vehicle;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.JWT.JWTUtil;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Response.AccountResponse;
import com.example.project2.models.DTOs.RegistrationUserDTO;

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
     * @return result, an account response with necesary fields
     * 
     * @throws DuplicateEmailException if email already exist in db,
     * DuplicateUsernameException if username already in db,
     * NoSuchAlgorithmException if hashing algorithm doesn't exist
     */

    public String register(Account account) throws DuplicateEmailException, DuplicateUsernameException, NoSuchAlgorithmException{


        Account check = accountRepository.findAccountByEmail(account.getEmail());
        if (check != null)
            throw new DuplicateEmailException();
        check = accountRepository.findAccountByUsername(account.getUsername());
        if (check != null)
            throw new DuplicateUsernameException();

        account.setPassword(toHexString(getSHA(account.getPassword())));

        Account res = accountRepository.save(account);

        
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


        if (!check.getPassword().equals(account.getPassword())) throw new PasswordIncorrectException();
        String token = JWTUtil.generateToken(check.getUsername(), String.valueOf(check.getAccountId()));
        AccountResponse result = new AccountResponse(
            check.getAccountId(), 
            check.getUsername(), 
            check.getRole(),
            check.getIsSuspended(),
            check.getImageId(),
            token
            );
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
            token
            );

        return result;
    }

    // Get user by ID
    public Optional<Account> getUserById(Integer userId) {
        return accountRepository.findById(userId.longValue());
    }

    // Update user profile
    public void updateUserProfile(Integer userId, RegistrationUserDTO updateUserDTO) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(userId.longValue());

        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }

        Account account = optionalAccount.get();
        account.setFirstName(updateUserDTO.getFirstName());
        account.setLastName(updateUserDTO.getLastName());
        account.setEmail(updateUserDTO.getEmail());
        account.setPhone(updateUserDTO.getPhone());
        // Update other fields as necessary

        accountRepository.save(account); // Save the updated account
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
