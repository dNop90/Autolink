package com.example.project2.Repositories;

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
    Account findAccountByAccountId(Long accountId);

    @Query("SELECT a FROM Account a WHERE a.username = ?1 OR a.email = ?1")
    Account findByUsernameOrEmail(String usernameOrEmail);

    @Modifying
    @Query("UPDATE Account SET role = ?2 WHERE accountId = ?1")
    int updateRole(Long accountId, Integer role);

    @Modifying
    @Query("UPDATE Account SET isSuspended = ?2 WHERE accountId =?1")
    int suspend(Long accountId, Boolean suspend);

    @Modifying
    @Query("UPDATE Account set password = ?2 WHERE accountId = ?1")
    int updatePassword(Long accountId, String pasword);

    @Modifying
    @Query("UPDATE Account set email = ?2, firstName = ?3, lastName =?4, phone =?5 WHERE accountId =?1")
    int updateUserProfile(Long accountId, String email, String firtName, String lastName, String phone);
}
