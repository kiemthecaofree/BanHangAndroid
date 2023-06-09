package com.example.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class User {
    private String token;
    private int id;
    private String username;
    private String fullName;
    private ArrayList<String> roles;

    public User() {
        roles = new ArrayList<String>();
    }

    public User(String token, int id, String username, String fullName, ArrayList<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
