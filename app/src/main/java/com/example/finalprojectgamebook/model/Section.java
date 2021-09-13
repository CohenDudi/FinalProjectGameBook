package com.example.finalprojectgamebook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Section implements Serializable {
    private String name;
    private String type;
    private String description;
    private Set<String> usersId;

    public Section(){}

    public Section(String name,String type,String description){
        this.name = name;
        this.type = type;
        this.description = description;
        this.usersId = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void addUserId(String s){
        usersId.add(s);
    }
    
}
