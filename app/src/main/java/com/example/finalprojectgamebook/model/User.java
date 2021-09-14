package com.example.finalprojectgamebook.model;

public class User {
    String name;
    String userId;

    public User(){}

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }


}
