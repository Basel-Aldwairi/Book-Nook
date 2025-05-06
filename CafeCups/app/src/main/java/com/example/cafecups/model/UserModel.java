package com.example.cafecups.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String email;
    private String username;
    private Timestamp createdTimestamp;

    public UserModel() {
    }

    public UserModel(String email, String username,Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        this.email = email;
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
