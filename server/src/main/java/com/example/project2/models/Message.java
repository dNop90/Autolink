package com.example.project2.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


/**
 * This is use for kafka data
 * It is NOT for the table entity
 */
public class Message {
    private Integer ID;
    private Long fromAccountID;
    private Long toAccountID;
    private String message;

    @CreationTimestamp
    private LocalDateTime createAt;

    public Message()
    {

    }

    public Message(Integer iD, Long fromAccountID, Long toAccountID, String message) {
        ID = iD;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.message = message;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
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
