package com.example.finalprojectgamebook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section implements Serializable {
    private String name;
    private String type;
    private String description;
    private List<String> usersId;
    private String img;

    public Section(){
        this.usersId = new ArrayList<>();
    }

    public Section(String name,String type,String description,String img){
        this.name = name;
        this.type = type;
        this.description = description;
        this.usersId = new ArrayList<>();
        this.img = img;
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

    public List<String> getUsersId() {
        return usersId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
