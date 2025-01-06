package com.example.project2.Services;

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
import com.example.project2.Response.ProfileResponse;

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
                token);
        return result;
    }

    /*
     * Service to retrive current logged in user and push token into cookie
     * 
     * @param username username of the logged in account, token token to pass back into cookie
     * @returns an accountResponse with the necessary fields
     */
    public AccountResponse getCurrentUser(String username, String token) {
        Account check = accountRepository.findAccountByUsername(username);
        AccountResponse result = new AccountResponse(
                check.getAccountId(),
                check.getUsername(),
                check.getRole(),
                check.getIsSuspended(),
                token);

        return result;
    }

    /*
     * Service to retrive a user's account information by a username
     * @param username of the user to look up
     * @returns a accountResponse with the necessary fields
     */
    public AccountResponse getUserByUsername(String username) throws AccountNotFoundException {
        Account check = accountRepository.findAccountByUsername(username);
        if (check == null)
            throw new AccountNotFoundException();
        AccountResponse result = new AccountResponse(
                check.getAccountId(),
                check.getUsername(),
                check.getRole(),
                check.getIsSuspended(),
                null);
        return result;
    }

    /*
     * Service to retrive a user's profile by a username
     * @param username of the user to look up
     * @returns a profileResponse with the necessary fields
     */
    public ProfileResponse getUserProfileByUsername(String username) throws AccountNotFoundException {
        Account check = accountRepository.findAccountByUsername(username);
        if (check == null)
            throw new AccountNotFoundException();

        ProfileResponse res = new ProfileResponse(
                check.getUsername(),
                check.getEmail(),
                check.getFirstName(),
                check.getLastName(),
                check.getPhone());
        return res;
    }

    /*
    * Service to update a user's profile
    * @param profile with the updated information
    */ 
    public void updateUserProfile(ProfileResponse profile) throws AccountNotFoundException {
        Account check = accountRepository.findAccountByUsername(profile.getUsername());
        if (check == null) {
            throw new AccountNotFoundException();
        }
        accountRepository.updateUserProfile(check.getAccountId(), profile.getEmail(), profile.getFirstName(),
                profile.getLastName(), profile.getPhone());
    }

    /*
    * Service the change a user's password
    * @param account with username, current password to check for validity, and new pasword to update to
    */
    public void updatePassword(Account account)
            throws AccountNotFoundException, NoSuchAlgorithmException, PasswordIncorrectException {
        Account check = accountRepository.findAccountByUsername(account.getUsername());
        if (check == null)
            throw new AccountNotFoundException();
        String currpwd = toHexString(getSHA(account.getCurrentPassword()));
        if (check.getPassword().equals(currpwd)) {
            String newpwd = toHexString(getSHA(account.getNewPassword()));
            accountRepository.updatePassword(check.getAccountId(), newpwd);
        } else
            throw new PasswordIncorrectException();
    }

    /*
     * Promotion service to change regular account to admin account
     * 
     * @param username, username of the account to be promoted
     * 
     * @return Promoted message
     * 
     * @throws AccountNotFoundException if username not in database
     */
    public String promote(String username) throws AccountNotFoundException {
        Account a = accountRepository.findAccountByUsername(username);
        if (a != null) {
            accountRepository.updateRole(a.getAccountId(), 3);
            return "Promoted";
        } else {
            throw new AccountNotFoundException();
        }
    }

    /*
     * Suspension service to prevent account login
     * 
     * @param username, username of the account to be suspended
     * 
     * @return Suspended message
     * 
     * @throws AccountNotFoundException if username not in database
     */
    public String suspend(String username, Boolean status) throws AccountNotFoundException {
        Account a = accountRepository.findAccountByUsername(username);
        if (a != null) {
            accountRepository.suspend(a.getAccountId(), status);
            if (status) {
                return "Suspended";
            } else {
                return "Activated";
            }
        } else {
            throw new AccountNotFoundException();
        }
    }

    /*
     * Helper class for password hasing
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
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
    public static String toHexString(byte[] hash) {
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

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
