package com.example.finalprojectgamebook.model;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private String name;
    private int min;
    private int max;

    private List<User> users;

    public Role(){
        this.users = new ArrayList<>();
    }
    public Role(String name, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean isUserInList(User s){
        if(users.size()>0)
        for (User u:users) {
            if(u.getUserId().equals(s.getUserId()))return true;
        }
        return false;
    }

    public void removeUserInList(User s){
        users.removeIf(t -> t.getUserId().equals(s.getUserId()));
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

    public void addMin(int min) {
        this.min+=min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}
