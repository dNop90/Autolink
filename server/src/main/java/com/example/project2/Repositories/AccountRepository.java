package com.example.project2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project2.Entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

    @Query("SELECT a FROM Account a WHERE a.username = ?1")
    Account findAccountByUsername(String username);

    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Account findAccountByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.accountId = ?1")
    Account findAccountByAccountId(Integer accountId);
}
