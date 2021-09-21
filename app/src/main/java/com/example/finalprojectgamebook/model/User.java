package com.example.finalprojectgamebook.model;

public class User {
    String name;
    String userId;
    String imgUri;

    Boolean newMsg;

    public User(){}

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.newMsg = false;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getNewMsg() {
        return newMsg;
    }

    public void readMsg(Boolean newMsg){
        this.newMsg = newMsg;
    }


}
