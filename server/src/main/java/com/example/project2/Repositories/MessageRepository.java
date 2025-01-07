package com.example.project2.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project2.Entities.Message;

import jakarta.transaction.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM (SELECT * FROM message WHERE (from_accountid = ?1 OR from_accountid = ?2) AND (to_accountid = ?1 OR to_accountid = ?2) ORDER BY create_at DESC LIMIT 20 OFFSET ?3) ORDER BY create_at ASC", nativeQuery = true)
    List<Message> findAllByFromAccountIDAndToAccountID(Long fromAccountID, Long toAccountID, int offset);

    @Modifying
    @Transactional
    @Query(value = "SELECT from_accountid, from_username, to_accountid, to_username FROM message WHERE (from_accountid = ?1 OR to_accountid = ?1) AND (from_accountid <> to_accountid) GROUP BY from_accountid, from_username, to_accountid, to_username", nativeQuery = true)
    List<?> findAllUniqueAccountUserInteractWith(Long fromAccountID);
}
