package com.example.project2.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private Long fromAccountID;

    @Column(nullable = false)
    private Long toAccountID;

    private String message;

    @CreationTimestamp
    private LocalDateTime createAt;

    public Message()
    {

    }

    public Message(Long fromAccountID, Long toAccountID, String message) {
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(Long fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public Long getToAccountID() {
        return toAccountID;
    }

    public void setToAccountID(Long toAccountID) {
        this.toAccountID = toAccountID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
