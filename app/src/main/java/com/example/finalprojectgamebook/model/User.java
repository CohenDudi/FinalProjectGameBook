package com.example.finalprojectgamebook.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    String name;
    String userId;
    String imgUri;
    List<String> favoriteSection;

    Boolean newMsg;

    public User(){
        this.favoriteSection = new ArrayList<>();
    }

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.newMsg = false;
        this.favoriteSection = new ArrayList<>();
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

    public List<String> getFavoriteSection() {
        return favoriteSection;
    }

    public void addFavorite(String section){
        favoriteSection.add(section);
    }

    public Boolean isFavorite(String section){
        if(favoriteSection!= null)
        for (String s:favoriteSection) {
            if (s.equals(section))return true;
        }
        return false;
    }

}
