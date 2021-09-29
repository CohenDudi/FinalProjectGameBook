package com.example.finalprojectgamebook.model;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private String name;
    private int min;
    private int max;


    List<User> users = new ArrayList<>();



    public Role(){}
    public Role(String name, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public void addUser(User user){
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean isUserInList(User user){
        for (User u:users) {
            if(u.getUserId().equals(user.getUserId()))return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
