package com.example.finalprojectgamebook.model;


public class ChatSection {
    String name;
    String userId;


    String msg;


    Boolean newMsg;

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getMsg() {
        return msg;
    }

    public ChatSection(){};
    public ChatSection(String name, String userId, String msg){
        this.name = name;
        this.userId = userId;
        this.msg = msg;
        this.newMsg = false;
    }

    public Boolean getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(Boolean newMsg) {
        this.newMsg = newMsg;
    }






}
