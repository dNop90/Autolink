package com.example.project2.Services;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Account;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Exceptions.DuplicateEmailException;
import com.example.project2.Exceptions.DuplicateUsernameException;
import com.example.project2.Exceptions.PasswordIncorrectException;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Response.AccountResponse;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse register(Account account) throws DuplicateEmailException, DuplicateUsernameException{
        Account check = accountRepository.findAccountByEmail(account.getEmail());
        if (check != null) throw new DuplicateEmailException();
        check = accountRepository.findAccountByUsername(account.getUsername());
        if (check != null) throw new DuplicateUsernameException();
        
        Account res = accountRepository.save(account);
        AccountResponse result = new AccountResponse(
            res.getAccountId(), 
            res.getUsername(), 
            res.getEmail(),
            res.getRole(),
            res.getIsSuspended(),
            res.getFirstName(),
            res.getLastName(),
            res.getPhone(),
            res.getImageId()
            );
        return result;
    }

    public AccountResponse login(Account account) throws  AccountNotFoundException, PasswordIncorrectException {
        Account check = accountRepository.findAccountByUsername(account.getUsername());
        
        if (check == null) throw new AccountNotFoundException();
        if (!check.getPassword().equals(account.getPassword())) throw new PasswordIncorrectException();

        AccountResponse result = new AccountResponse(
            check.getAccountId(), 
            check.getUsername(), 
            check.getEmail(),
            check.getRole(),
            check.getIsSuspended(),
            check.getFirstName(),
            check.getLastName(),
            check.getPhone(),
            check.getImageId()
            );
        return result;
    }

}