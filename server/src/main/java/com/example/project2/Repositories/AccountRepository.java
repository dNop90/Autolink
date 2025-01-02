package com.example.project2.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT a FROM Account a WHERE a.username = ?1 OR a.email = ?1")
    Optional<Account> findByUsernameOrEmail(String usernameOrEmail);

    @Modifying
    @Query("UPDATE Account SET role = ?2 WHERE accountId = ?1")
    int updateRole(Long accountId, Integer role);
}
